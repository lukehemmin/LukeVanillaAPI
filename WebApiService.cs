using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting; // 이미 추가됨
using Microsoft.Extensions.Configuration;
using LukeVanillaAPI.discord;
using System.Threading;
using System.Threading.Tasks;

namespace LukeVanillaAPI
{
    public class WebApiService : IHostedService
    {
        private readonly IServiceProvider _serviceProvider;
        private IHost? _webHost;

        public WebApiService(IServiceProvider serviceProvider)
        {
            _serviceProvider = serviceProvider;
        }

        public async Task StartAsync(CancellationToken cancellationToken)
        {
            _webHost = Host.CreateDefaultBuilder()
                .ConfigureWebHostDefaults(webBuilder =>
                {
                    webBuilder
                        .UseUrls("http://0.0.0.0:5000")
                        .ConfigureServices(services =>
                        {
                            // 기존 서비스들을 다시 등록
                            foreach (var service in _serviceProvider.GetServices<object>())
                            {
                                services.AddSingleton(service.GetType(), service);
                            }
                        })
                        .Configure(app =>
                        {
                            var discordService = _serviceProvider.GetRequiredService<DiscordService>();

                            app.UseRouting();

                            app.UseEndpoints(endpoints =>
                            {
                                endpoints.MapGet("/api/user/{discordId}", async context =>
                                {
                                    var discordIdStr = context.Request.RouteValues["discordId"]?.ToString();
                                    if (ulong.TryParse(discordIdStr, out ulong discordId))
                                    {
                                        var exists = discordService.UserExists(discordId);
                                        var response = new { exists };
                                        await context.Response.WriteAsJsonAsync(response);
                                    }
                                    else
                                    {
                                        context.Response.StatusCode = 400;
                                        await context.Response.WriteAsync("Invalid Discord ID");
                                    }
                                });
                            });
                        });
                })
                .Build();

            await _webHost.StartAsync(cancellationToken);

            // Discord 서비스 초기화
            var discordServiceInstance = _serviceProvider.GetRequiredService<DiscordService>();
            await discordServiceInstance.InitializeAsync();

            // Discord 봇 실행
            var discordBot = _serviceProvider.GetRequiredService<DiscordBot>();
            _ = discordBot.RunAsync(); // 비동기로 실행
        }

        public async Task StopAsync(CancellationToken cancellationToken)
        {
            if (_webHost != null)
            {
                await _webHost.StopAsync(cancellationToken);
                _webHost.Dispose();
            }
        }
    }
}