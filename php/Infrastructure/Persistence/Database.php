<?php

namespace Infrastructure\Persistence;
use PDO;
use PDOException;

class Database
{

    // プロパティ（データベース接続情報）
    private $db_host;
    private $db_port;
    private $db_user;
    private $db_password;
    private $db_name;

    // コンストラクタ（インスタンス化時に呼ばれる）
    public function __construct($db_host, $db_port, $db_user, $db_password, $db_name)
    {
        // 引数で受け取った値をプロパティに設定
        $this->db_host = $db_host;
        $this->db_port = $db_port;
        $this->db_user = $db_user;
        $this->db_password = $db_password;
        $this->db_name = $db_name;
    }

    // データベース接続情報を取得するメソッド
    public function getDbHost()
    {
        return $this->db_host;
    }

    public function getDbPort()
    {
        return $this->db_port;
    }

    public function getDbUser()
    {
        return $this->db_user;
    }

    public function getDbPassword()
    {
        return $this->db_password;
    }

    public function getDbName()
    {
        return $this->db_name;
    }

    // データベース接続メソッド
    public function connect()
    {
        try {
            // DSN（Data Source Name）を作成
            $dsn = "pgsql:host={$this->db_host};port={$this->db_port};dbname={$this->db_name}";
            // PDOインスタンスを作成し、接続
            $this->pdo = new PDO($dsn, $this->db_user, $this->db_password);
            // 接続成功時にはエラーモードを設定
            $this->pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        } catch (PDOException $e) {
            // 接続失敗時にエラーメッセージを表示
            echo "データベース接続に失敗しました: " . $e->getMessage();
        }
    }

    // クエリ実行メソッド（例）
    public function query($query)
    {
        try {
            // クエリを実行
            $stmt = $this->pdo->query($query);
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            // エラーハンドリング
            echo "クエリ実行に失敗しました: " . $e->getMessage();
        }
    }

    // 接続を閉じるメソッド
    public function close()
    {
        $this->pdo = null;
    }
}


// ****************** 作成したPDOインスタンスを返す ******************
function DB_access($host_name, $port_no, $db_user, $db_pass, $db_name, $sql = "")
{
    $dsn = "pgsql:dbname={$db_name} host={$host_name} port={$port_no}";
    $user = $db_user;
    $password = $db_pass;

    $dbh = new PDO($dsn, $user, $password);

    if ($sql != "") {
        // ******************** セッション管理開始 ********************
        if (session_status() == PHP_SESSION_NONE) {
            // セッションは有効で、開始していないとき
            session_start();
        }
    }

    return $dbh;
}

function DB_exist($host_name, $port_no, $db_user, $db_pass, $db_dummy, $db_name)
{

    $dsn = "pgsql:dbname={$db_dummy} host={$host_name} port={$port_no}";
    $user = $db_user;
    $password = $db_pass;

    try {
        $dbh = new PDO($dsn, $user, $password);
        $sql = 'SELECT * FROM pg_database;';
        $result = $dbh->query($sql);
        $exist = False;
        foreach ($result as $loop) {
            if ($loop[1] == $db_name) {
                $exist = True;
            }
        }
        return $exist;

    } catch (PDOException $e) {
        print('Error:' . $e->getMessage());
        die();
    }
}