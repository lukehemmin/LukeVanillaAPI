using System;
using System.Threading.Tasks;
using Discord;
using Discord.WebSocket;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

using LukeVanillaAPI.discord;
using LukeVanillaAPI;

internal class Program
{
    private static async Task Main(string[] args)
    {
        var serviceCollection = new ServiceCollection();
        ConfigureServices(serviceCollection);

        var serviceProvider = serviceCollection.BuildServiceProvider();

        var bot = serviceProvider.GetRequiredService<DiscordBot>();
        await bot.RunAsync();
    }

    private static void ConfigureServices(IServiceCollection services)
    {
        var configuration = Config.InitializeConfiguration();

        services.AddSingleton<IConfiguration>(configuration);
        
        // DiscordSocketClient를 올바른 Configuration으로 초기화
        services.AddSingleton(new DiscordSocketClient(new DiscordSocketConfig
        {
            GatewayIntents = GatewayIntents.Guilds |
                             GatewayIntents.GuildMessages |
                             GatewayIntents.MessageContent
        }));

        services.AddSingleton<Database>();
        services.AddSingleton<AuthChat>();
        services.AddSingleton<DiscordBot>();
    }
}