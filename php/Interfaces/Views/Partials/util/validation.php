<?php

/*
 * 必須チェック
 */
function validateRequiredCheck($object):bool {
    if($object == null){
        return false;
    }
    return true;
}

/*
 * 空欄チェック
 */
function validateBlankCheck(string $object):bool {
    if($object == ""){
        return false;
    }
    return true;
}

/*
 * 桁数チェック
 */
function validateLengthCheck(string|int $object,int $min, int $max):bool {
    $str = (string)$object;
    $len = strlen($str);
    if ($len === 0) {
        return false;
    }
    return $len >= $min && $len <= $max;
}

function validateMinLengthCheck(string|int $object, int $min): bool {
    $str = (string)$object;
    return mb_strlen($str, "UTF-8") >= $min;
}

function validateMaxLengthCheck(string|int $object, int $max): bool {
    $str = (string)$object;
    return mb_strlen($str, "UTF-8") <= $max;
}

/*
 * 数値の範囲チェック
 */
function validateNumberRange(int|float $object, int|float $min, int|float $max): bool {
    // 数値以外なら例外を投げる
    if (!is_numeric($object)) {
        throw new InvalidArgumentException("バリデーション（数値範囲チェック）：数値以外が指定されています");
    }
    return $object >= $min && $object <= $max;
}
function validateMinValue(int|float $object, int|float $min): bool {
    if (!is_numeric($object)) {
        throw new InvalidArgumentException("バリデーション（数値最小値チェック）：数値以外が指定されています");
    }
    return $object >= $min;
}
function validateMaxValue(int|float $object, int|float $max): bool {
    if (!is_numeric($object)) {
        throw new InvalidArgumentException("バリデーション（数値最大値チェック）：数値以外が指定されています");
    }
    return $object <= $max;
}

/*
 * 形式チェック（数値）
 */
function validateNumericCheck(string|int $object): bool {
    $str = (string)$object;
    return preg_match('/^[0-9]+$/', $str) === 1;
}

/*
 * 形式チェック（日付）
 */
function validateDateCheck(string $object, $format = 'Y-m-d'): bool {
    $d = DateTime::createFromFormat($format, $object);
    return $d && $d->format($format) === $object;
}