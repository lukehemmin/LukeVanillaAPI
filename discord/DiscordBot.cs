// DiscordBot.cs 수정
using System;
using System.IO;
using System.Threading.Tasks;
using Discord;
using Discord.WebSocket;
using LukeVanillaAPI.discord;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json.Linq;

public class DiscordBot
{
    private readonly DiscordSocketClient _client;
    private readonly IConfiguration _config;
    private readonly AuthChat _authChat;

    public DiscordBot(IConfiguration config, AuthChat authChat, DiscordSocketClient client) // 수정된 부분
    {
        _config = config;
        _authChat = authChat;
        _client = client;

        _client.LoginAsync(TokenType.Bot, _config["Discord:Token"]).Wait();
        _client.StartAsync().Wait();

        _client.Log += LogAsync;
        _client.Ready += ReadyAsync;
        _client.MessageReceived += MessageReceivedAsync;
    }

    public async Task RunAsync()
    {
        // 터미널 입력 처리
        _ = Task.Run(async () =>
        {
            while (true)
            {
                var input = Console.ReadLine();
                if (input == "reset-slash-commands")
                {
                    await ResetSlashCommandsAsync();
                    Console.WriteLine("등록된 슬래시 명령어가 모두 삭제되었습니다.");
                }
            }
        });

        // 프로그램이 종료될 때까지 대기
        await Task.Delay(-1);
    }

    private async Task ResetSlashCommandsAsync()
    {
        try
        {
            await _client.BulkOverwriteGlobalApplicationCommandsAsync(new ApplicationCommandProperties[0]);
            Console.WriteLine("등록된 슬래시 명령어가 모두 삭제되었습니다.");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"슬래시 명령어 삭제 중 오류 발생: {ex.Message}");
        }
    }

    private Task LogAsync(LogMessage log)
    {
        Console.WriteLine(log.ToString());
        return Task.CompletedTask;
    }

    private Task ReadyAsync()
    {
        Console.WriteLine($"{_client.CurrentUser}에 연결되었습니다!");
        return Task.CompletedTask;
    }

    private Task MessageReceivedAsync(SocketMessage message)
    {
        if (message.Author.IsBot) return Task.CompletedTask;

        Console.WriteLine($"수신된 메시지: '{message.Content}' from Channel ID: {message.Channel.Id}");

        if (message.Content == "!ping")
        {
            return message.Channel.SendMessageAsync("Pong!");
        }
        else
        {
            // HandleAuthCodeAsync를 비동기적으로 실행
            _ = _authChat.HandleAuthCodeAsync(message);
            return Task.CompletedTask;
        }
    }
}