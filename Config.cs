using System;
using System.IO;
using Newtonsoft.Json.Linq;
using Microsoft.Extensions.Configuration;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LukeVanillaAPI
{
    internal class Config
    {
        public static IConfiguration InitializeConfiguration()
        {
            var configFilePath = Path.Combine(Directory.GetCurrentDirectory(), "appsettings.json");
            if (!File.Exists(configFilePath))
            {
                var defaultConfig = new JObject(
                    new JProperty("Discord", new JObject(
                        new JProperty("Token", "YOUR_BOT_TOKEN"),
                        new JProperty("ApplicationId", "YOUR_APPLICATION_ID")
                    )),
                    new JProperty("Database", new JObject(
                        new JProperty("Server", "your_server_ip"),
                        new JProperty("Port", "your_port"),
                        new JProperty("Database", "your_database"),
                        new JProperty("User", "your_user"),
                        new JProperty("Password", "your_password")
                    ))
                );
                File.WriteAllText(configFilePath, defaultConfig.ToString());
                Console.WriteLine("appsettings.json 파일이 생성되었습니다. 설정을 업데이트해주세요. ./appsettings.json");
                Environment.Exit(0); // 설정 업데이트를 위해 프로그램 종료
            }

            var configuration = new ConfigurationBuilder()
                .SetBasePath(Directory.GetCurrentDirectory())
                .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
                .Build();

            // 토큰 확인
            var token = configuration["Discord:Token"];
            if (string.IsNullOrEmpty(token) || token == "YOUR_BOT_TOKEN")
            {
                Console.WriteLine("봇 토큰을 업데이트해주세요. ./appsettings.json");
                Environment.Exit(0);
            }

            // 애플리케이션 ID 확인
            var appId = configuration["Discord:ApplicationId"];
            if (string.IsNullOrEmpty(appId) || appId == "YOUR_APPLICATION_ID")
            {
                Console.WriteLine("애플리케이션 ID를 업데이트해주세요. ./appsettings.json");
                Environment.Exit(0);
            }

            return configuration;
        }
    }
}