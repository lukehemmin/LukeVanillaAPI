using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MySqlConnector;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;

namespace LukeVanillaAPI
{
    public class Database
    {
        private readonly string connectionString;

        public Database(IConfiguration configuration)
        {
            var server = configuration["Database:Server"];
            var port = configuration["Database:Port"];
            var database = configuration["Database:Database"];
            var user = configuration["Database:User"];
            var password = configuration["Database:Password"];

            connectionString = $"Server={server};Port={port};Database={database};User ID={user};Password={password};";
        }

        public async Task<MySqlConnection> GetConnectionAsync()
        {
            var connection = new MySqlConnection(connectionString);
            await connection.OpenAsync();
            return connection;
        }
    }
}
