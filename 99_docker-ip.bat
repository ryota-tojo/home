cd ./
docker inspect -f "{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}" database_container
docker inspect -f "{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}" app_container
docker inspect -f "{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}" api_container
pause