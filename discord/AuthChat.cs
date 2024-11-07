using MySqlConnector;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LukeVanillaAPI.discord
{
    internal class AuthChat
    {
        private readonly string connectionString = "Server=localhost;Database=your_database;User ID=your_user;Password=your_password;";

        public async Task GetDataAsync()
        {
            using (var connection = new MySqlConnection(connectionString))
            {
                await connection.OpenAsync();

                string query = "SELECT * FROM your_table";
                using (var command = new MySqlCommand(query, connection))
                using (var reader = await command.ExecuteReaderAsync())
                {
                    while (await reader.ReadAsync())
                    {
                        // 데이터를 처리합니다
                    }
                }
            }
        }
    }
}
