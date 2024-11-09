using System;
using System.Threading.Tasks;
using Discord;
using Discord.WebSocket;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using LukeVanillaAPI.discord;
using LukeVanillaAPI;

internal class Program
{
    private static async Task Main(string[] args)
    {
        var host = Host.CreateDefaultBuilder(args)
            .ConfigureServices((context, services) =>
            {
                var configuration = Config.InitializeConfiguration();

                services.AddSingleton<IConfiguration>(configuration);

                services.AddSingleton(new DiscordSocketClient(new DiscordSocketConfig
                {
                    GatewayIntents = GatewayIntents.Guilds |
                                     GatewayIntents.GuildMessages |
                                     GatewayIntents.MessageContent
                }));

                services.AddSingleton<Database>();
                services.AddSingleton<AuthChat>();
                services.AddSingleton<DiscordService>();
                services.AddSingleton<DiscordBot>();

                // 웹 API 서비스 등록
                services.AddHostedService<WebApiService>();
            })
            .Build();

        await host.RunAsync();
    }
}