<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/member/get_member.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/category/get_category.php';

function autoActivateForSearchTemplate($groups_id)
{
    $cnt = 0;
    $template_list = getSearchTemplateList($groups_id);
    foreach ($template_list as $template) {
        if ($template['use'] == 1 and $template['deleted'] == 1) {
            $active_flag1 = False;
            $active_flag2 = False;
            if ($template['member_id'] != null) {
                $member = getMember($template['member_id'], $groups_id);
                if ($member['delete_flag'] != UI_ITEM_LABEL_NULL and $member['delete_flag'] == "0") {
                    $active_flag1 = True;
                }
            }else{
                $active_flag1 = True;
            }
            if ($template['category_id'] != null) {
                $category = getCategory($template['category_id'], $groups_id);
                if ($category['delete_flag'] != UI_ITEM_LABEL_NULL and $category['delete_flag'] == "0") {
                    $active_flag2 = True;
                }
            }else{
                $active_flag2 = True;
            }
            if ($active_flag1 == True and $active_flag2 == True) {
                apiCallSearchTemplateUnDisable($groups_id, $template['template_id']);
                $cnt++;
            }
        }
    }
    if ($cnt > 0) {
        $data = [
            "status" => "success",
            "message" => UI_ITEM_MEMBER . ", " . UI_ITEM_CATEGORY . "が参照できました。<br>" . $cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "を有効化しました"
        ];
    } else {
        $data = [
            "status" => "skip",
            "message" => ""
        ];
    }
    return json_encode($data);
}

function autoDeactivateForSearchTemplate($groups_id)
{
    $cnt = 0;
    $template_list = getSearchTemplateList($groups_id);
    foreach ($template_list as $template) {
        if ($template['deleted'] == 0) {
            $de_active_flag1 = False;
            $de_active_flag2 = False;
            if ($template['member_id'] != null) {
                $member = getMember($template['member_id'], $groups_id);
                if ($member['delete_flag'] == UI_ITEM_LABEL_NULL or $member['delete_flag'] == "1") {
                    $de_active_flag1 = True;
                }
            }
            if ($template['category_id'] != null) {
                $category = getCategory($template['category_id'], $groups_id);
                if ($category['delete_flag'] == UI_ITEM_LABEL_NULL or $category['delete_flag'] == "1") {
                    $de_active_flag2 = True;
                }
            }
            if ($de_active_flag1 == True or $de_active_flag2 == True) {
                apiCallSearchTemplateDisable($groups_id, $template['template_id']);
                $cnt++;
            }
        }
    }
    if ($cnt > 0) {
        $data = [
            "status" => "success",
            "message" => UI_ITEM_MEMBER . "または" . UI_ITEM_CATEGORY . "が参照できませんでした。<br>" . $cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "を無効化しました"
        ];
    } else {
        $data = [
            "status" => "skip",
            "message" => ""
        ];
    }
    return json_encode($data);
}