import threading
import os
import sys
import subprocess

# ********** 実行中ファイルのフォルダーを取得する **********
myfile = os.path.abspath(__file__)
parent_folder = os.path.abspath(os.path.join(myfile, os.pardir))
parent_folder = os.path.abspath(os.path.join(parent_folder, os.pardir))
db_info_ini=f"{parent_folder}\\php\\config\\db_info.ini"

print(f"db_info.ini = '{db_info_ini}'")

# ********** dbコンテナのIPを取得 **********
command = 'docker inspect -f "{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}" database_container'
result = subprocess.run(command, shell=True, capture_output=True, text=True)
db_container_ip=result.stdout.replace("\n","")

print(f"container_ip = '{db_container_ip}'")

# ********** IPを更新したdb_infoデータを生成 **********
file_text=""
f = open(db_info_ini, 'r')
datalist = f.readlines()
for data in datalist:
    row_data=data.replace("\n","")
    if row_data.find('host')!=-1:
        if file_text=="":
            file_text=f"host = {db_container_ip}"
        else:
            file_text=f"{file_text}\nhost = {db_container_ip}"
    else:
        if file_text=="":
            file_text=f"{row_data}"
        else:
            file_text=f"{file_text}\n{row_data}"
f.close()

# ファイルの内容を上書きする
f = open(db_info_ini, 'w')
f.write(file_text)
f.close()
