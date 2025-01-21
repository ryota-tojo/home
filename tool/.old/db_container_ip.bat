rem コンテナのipを表示
docker inspect -f "{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}" database_container

pause