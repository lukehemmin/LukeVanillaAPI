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

        // Check if appsettings.json exists, if not create it
        var configFilePath = Path.Combine(Directory.GetCurrentDirectory(), "appsettings.json");
        if (!File.Exists(configFilePath))
        {
            var defaultConfig = new JObject(
                new JProperty("Discord", new JObject(
                    new JProperty("Token", "YOUR_BOT_TOKEN")
                ))
            );
            File.WriteAllText(configFilePath, defaultConfig.ToString());
            Console.WriteLine("appsettings.json file created. Please update it with your bot token. ./appsettings.json");
            Environment.Exit(0); // Exit the application so the user can update the token
        }

        // Load configuration
        var builder = new ConfigurationBuilder()
            .SetBasePath(Directory.GetCurrentDirectory())
            .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true);
        _config = builder.Build();

        // Check if the token is still the default value
        var token = _config["Discord:Token"];
        if (token == "YOUR_BOT_TOKEN")
        {
            Console.WriteLine("Please update it with your bot token. ./appsettings.json");
            Environment.Exit(0); // Exit the application so the user can update the token
        }
    }

    public async Task RunAsync()
    {
        var token = _config["Discord:Token"]; // appsettings.json에서 토큰을 가져옴

        await _client.LoginAsync(TokenType.Bot, token);
        await _client.StartAsync();

        // Block this task until the program is closed.
        await Task.Delay(-1);
    }

    private Task LogAsync(LogMessage log)
    {
        Console.WriteLine(log.ToString());
        return Task.CompletedTask;
    }

    private Task ReadyAsync()
    {
        Console.WriteLine($"{_client.CurrentUser} is connected!");
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
