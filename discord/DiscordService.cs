using Discord; // 추가된 네임스페이스
using Discord.WebSocket;
using Microsoft.Extensions.Configuration;
using System;
using System.Threading.Tasks;

namespace LukeVanillaAPI.discord
{
    public class DiscordService
    {
        private readonly DiscordSocketClient _client;
        private readonly IConfiguration _config;
        private readonly ulong _guildId;

        public DiscordService(DiscordSocketClient client, IConfiguration config)
        {
            _client = client;
            _config = config;
            _guildId = ulong.Parse(_config["Discord:GuildId"]);
        }

        public async Task InitializeAsync()
        {
            _client.Log += LogAsync;
            await _client.LoginAsync(TokenType.Bot, _config["Discord:Token"]);
            await _client.StartAsync();
        }

        private Task LogAsync(LogMessage log)
        {
            Console.WriteLine(log.ToString());
            return Task.CompletedTask;
        }

        public bool UserExists(ulong discordId)
        {
            var guild = _client.GetGuild(_guildId);
            var user = guild.GetUser(discordId);
            return user != null;
        }
    }
}