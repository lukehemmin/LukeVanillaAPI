using MySqlConnector;
using System.Threading.Tasks;
using Discord;
using Discord.WebSocket;
using Microsoft.Extensions.Configuration;
using System.Text.RegularExpressions;

namespace LukeVanillaAPI.discord
{
    public class AuthChat
    {
        private readonly Database _database;
        private readonly DiscordSocketClient _client;
        private readonly IConfiguration _config;

        public AuthChat(Database database, DiscordSocketClient client, IConfiguration config)
        {
            _database = database;
            _client = client;
            _config = config;
        }

        public async Task HandleAuthCodeAsync(SocketMessage message)
        {
            // 인증 채널 ID 가져오기
            var authChannelIdStr = await GetSettingValueAsync("AuthChannel");
            var authLogChannelIdStr = await GetSettingValueAsync("AuthLogChannel");
            var authRoleIdStr = await GetSettingValueAsync("DiscordAuthRole");

            if (!ulong.TryParse(authChannelIdStr, out ulong authChannelId) ||
                !ulong.TryParse(authLogChannelIdStr, out ulong authLogChannelId) ||
                !ulong.TryParse(authRoleIdStr, out ulong authRoleId))
            {
                return;
            }

            // 인증 채널이 아니면 무시
            if (message.Channel.Id != authChannelId)
                return;

            // 인증 코드 형식 확인
            var regex = new Regex("^[A-Z0-9]{6}$");
            if (!regex.IsMatch(message.Content))
            {
                // 메시지 삭제
                await message.DeleteAsync();

                // 오류 메시지 전송
                var reply = await message.Channel.SendMessageAsync(":x: 인증코드가 올바르지 않습니다.");
                await Task.Delay(60000);
                await reply.DeleteAsync();
                return;
            }

            // 메시지 삭제
            await message.DeleteAsync();

            using var conn = await _database.GetConnectionAsync();
            var cmd = new MySqlCommand("SELECT AuthCode, IsAuth, UUID FROM Player_Auth WHERE AuthCode = @AuthCode", conn);
            cmd.Parameters.AddWithValue("@AuthCode", message.Content);
            using var reader = await cmd.ExecuteReaderAsync();

            if (!await reader.ReadAsync())
            {
                var reply = await message.Channel.SendMessageAsync(":x: 인증코드가 올바르지 않습니다.");
                await Task.Delay(60000);
                await reply.DeleteAsync();
                return;
            }

            var isAuth = reader.GetBoolean("IsAuth");
            var uuid = reader.GetString("UUID");
            reader.Close();

            if (isAuth)
            {
                var reply = await message.Channel.SendMessageAsync(":white_check_mark: 이미 인증이 완료되었습니다.");
                await Task.Delay(60000);
                await reply.DeleteAsync();
                return;
            }

            // 인증 완료 처리
            var updateCmd = new MySqlCommand("UPDATE Player_Auth SET IsAuth = 1 WHERE AuthCode = @AuthCode", conn);
            updateCmd.Parameters.AddWithValue("@AuthCode", message.Content);
            await updateCmd.ExecuteNonQueryAsync();

            // DiscordID 업데이트: Player_Data 테이블에 Discord ID 저장
            var updateDiscordIdCmd = new MySqlCommand("UPDATE Player_Data SET DiscordID = @DiscordID WHERE UUID = @UUID", conn);
            updateDiscordIdCmd.Parameters.AddWithValue("@DiscordID", message.Author.Id.ToString());
            updateDiscordIdCmd.Parameters.AddWithValue("@UUID", uuid);
            await updateDiscordIdCmd.ExecuteNonQueryAsync();

            // 역할 할당
            var guild = (message.Channel as SocketGuildChannel)?.Guild;
            if (guild != null)
            {
                var user = guild.GetUser(message.Author.Id);
                if (user != null)
                {
                    var role = guild.GetRole(authRoleId);
                    if (role != null)
                    {
                        await user.AddRoleAsync(role);
                    }
                }
            }

            var replySuccess = await message.Channel.SendMessageAsync(":white_check_mark: 인증이 완료되었습니다.");
            await Task.Delay(60000);
            await replySuccess.DeleteAsync();

            // 닉네임 가져오기
            var nicknameCmd = new MySqlCommand("SELECT NickName FROM Player_Data WHERE UUID = @UUID", conn);
            nicknameCmd.Parameters.AddWithValue("@UUID", uuid);
            var nickname = (string)await nicknameCmd.ExecuteScalarAsync();

            // 인증 로그 메시지 전송
            var authLogChannel = _client.GetChannel(authLogChannelId) as IMessageChannel;
            if (authLogChannel != null)
            {
                await authLogChannel.SendMessageAsync($"{message.Author.Mention} 님이 {nickname} ({uuid}) 로 인증하였습니다.");
            }
        }

        private async Task<string> GetSettingValueAsync(string settingType)
        {
            using var conn = await _database.GetConnectionAsync();
            var cmd = new MySqlCommand("SELECT setting_value FROM Settings WHERE setting_type = @SettingType", conn);
            cmd.Parameters.AddWithValue("@SettingType", settingType);
            var result = await cmd.ExecuteScalarAsync();
            return result?.ToString() ?? string.Empty;
        }
    }
}