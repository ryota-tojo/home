<?php
// データベース接続情報
define("DB_HOST", getenv('DB_HOST'));
define("DB_PORT", getenv('DB_PORT'));
define("DB_USER", getenv('DB_USER'));
define("DB_PASSWORD", getenv('DB_PASSWORD'));
define("DB_NAME", getenv('DB_NAME'));


// api
define("API_SERVICE_NAME", getenv('API_NAME'));
define("API_SERVICE_PORT", getenv('API_PORT'));
define("API_BASE_URL", "http://" . API_SERVICE_NAME . ":" . API_SERVICE_PORT . "/");
// 使用不可文字
define("INVALID_USER_NAME", ["admin"]);
define("INVALID_GROUPS_ID", ["maintenance"]);

?>