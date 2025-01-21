<?php

function encode($str){
    return base64_encode($str);
}
function decode($str){
    return base64_decode($str);
}
function isBase64($str) {
    return $str == base64_encode(base64_decode($str))?true : false;
}
?>