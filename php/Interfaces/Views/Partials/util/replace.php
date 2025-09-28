<?php

/*
 * %s文字列置換
 */
function replacement($format, $replace_ward_str):string {

    echo "{$format}/ ";
    echo "{$replace_ward_str}<br>";

    $replace_wards = [];
    if (!empty($replace_ward_str)) {
        $replace_wards = explode(",", $replace_ward_str);
    }

    $placeholders = substr_count($format, '%s');

    if ($placeholders > 0 && count($replace_wards) >= $placeholders) {
        $result_str = vsprintf($format, array_slice($replace_wards, 0, $placeholders));
    } else {
        // 置換なし
        $result_str = $format;
    }

    return $result_str;
}
