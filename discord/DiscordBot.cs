// DiscordBot.cs 수정
using System;
using System.IO;
using System.Threading.Tasks;
using Discord;
using Discord.WebSocket;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json.Linq;

public class DiscordBot
{
    private readonly DiscordSocketClient _client;
    private readonly IConfiguration _config;

    public DiscordBot()
    {
        _client = new DiscordSocketClient();
        _client.Log += LogAsync;
        _client.Ready += ReadyAsync;
        _client.MessageReceived += MessageReceivedAsync;

        // appsettings.json 파일 확인 및 생성
        var configFilePath = Path.Combine(Directory.GetCurrentDirectory(), "appsettings.json");
        if (!File.Exists(configFilePath))
        {
            var defaultConfig = new JObject(
                new JProperty("Discord", new JObject(
                    new JProperty("Token", "YOUR_BOT_TOKEN"),
                    new JProperty("ApplicationId", "YOUR_APPLICATION_ID")
                ))
            );
            File.WriteAllText(configFilePath, defaultConfig.ToString());
            Console.WriteLine("appsettings.json 파일이 생성되었습니다. 봇 토큰과 애플리케이션 ID를 업데이트해주세요. ./appsettings.json");
            Environment.Exit(0); // 설정 업데이트를 위해 프로그램 종료
        }

        // 설정 로드
        var builder = new ConfigurationBuilder()
            .SetBasePath(Directory.GetCurrentDirectory())
            .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true);
        _config = builder.Build();

        // 토큰 확인
        var token = _config["Discord:Token"];
        if (token == "YOUR_BOT_TOKEN")
        {
            Console.WriteLine("봇 토큰을 업데이트해주세요. ./appsettings.json");
            Environment.Exit(0); // 설정 업데이트를 위해 프로그램 종료
        }

        // 애플리케이션 ID 확인
        var appId = _config["Discord:ApplicationId"];
        if (appId == "YOUR_APPLICATION_ID")
        {
            Console.WriteLine("애플리케이션 ID를 업데이트해주세요. ./appsettings.json");
            Environment.Exit(0); // 설정 업데이트를 위해 프로그램 종료
        }
    }

    public async Task RunAsync()
    {
        var token = _config["Discord:Token"];

        await _client.LoginAsync(TokenType.Bot, token);
        await _client.StartAsync();

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

    private async Task MessageReceivedAsync(SocketMessage message)
    {
        if (message.Author.IsBot) return;

        if (message.Content == "!ping")
        {
            await message.Channel.SendMessageAsync("Pong!");
        }
    }
}