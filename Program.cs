using System;
using System.Threading.Tasks;
using Discord;
using Discord.WebSocket;

internal class Program
{
    private static void Main(string[] args)
    {
        var bot = new DiscordBot();
        bot.RunAsync().GetAwaiter().GetResult();
    }
}