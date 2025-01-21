<?php

namespace Domain\Models;
class SettingMaster
{
    private $db_config;

    // プロパティの定義
    private $account_lockout_count;
    private $maintenance;
    private $slack_report_send_flg;
    private $slack_report_webhookurl;
    private $slack_send_test;
    private $layout;
    private $admin_userdata_view;
    private $admin_notice_view;
    private $admin_notice_initial_title;
    private $admin_notice_initial_content;
    private $user_input_history_view;
    private $user_management_view;
    private $user_analysis_graph_size_pc_width;
    private $user_analysis_graph_size_pc_height;
    private $user_analysis_graph_size_sp_width;
    private $user_analysis_graph_size_sp_height;
    private $user_analysis_graph_size_tb_width;
    private $user_analysis_graph_size_tb_height;
    private $user_communication_histry_view;
    private $user_communication_list_view;
    private $user_communication_view_conditions;

    public function __construct($db_config)
    {
        $this->db_config = $db_config;

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='account_lockout_count';");
        if ($results) {
            foreach ($results as $row) {
                $this->account_lockout_count = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='maintenance';");
        if ($results) {
            foreach ($results as $row) {
                $this->maintenance = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='slack_report_send_flg';");
        if ($results) {
            foreach ($results as $row) {
                $this->slack_report_send_flg = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='slack_report_webhookurl';");
        if ($results) {
            foreach ($results as $row) {
                $this->slack_report_webhookurl = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='slack_send_test';");
        if ($results) {
            foreach ($results as $row) {
                $this->slack_send_test = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='layout';");
        if ($results) {
            foreach ($results as $row) {
                $this->layout = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='admin_userdata_view';");
        if ($results) {
            foreach ($results as $row) {
                $this->admin_userdata_view = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='admin_notice_view';");
        if ($results) {
            foreach ($results as $row) {
                $this->admin_notice_view = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='admin_notice_initial_title';");
        if ($results) {
            foreach ($results as $row) {
                $this->admin_notice_initial_title = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='admin_notice_initial_content';");
        if ($results) {
            foreach ($results as $row) {
                $this->admin_notice_initial_content = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='user_input_history_view';");
        if ($results) {
            foreach ($results as $row) {
                $this->user_input_history_view = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='user_management_view';");
        if ($results) {
            foreach ($results as $row) {
                $this->user_management_view = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='user_analysis_graph_size_pc_width';");
        if ($results) {
            foreach ($results as $row) {
                $this->user_analysis_graph_size_pc_width = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='user_analysis_graph_size_pc_height';");
        if ($results) {
            foreach ($results as $row) {
                $this->user_analysis_graph_size_pc_height = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='user_analysis_graph_size_sp_width';");
        if ($results) {
            foreach ($results as $row) {
                $this->user_analysis_graph_size_sp_width = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='user_analysis_graph_size_sp_height';");
        if ($results) {
            foreach ($results as $row) {
                $this->user_analysis_graph_size_sp_height = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='user_analysis_graph_size_tb_width';");
        if ($results) {
            foreach ($results as $row) {
                $this->user_analysis_graph_size_tb_width = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='user_analysis_graph_size_tb_height';");
        if ($results) {
            foreach ($results as $row) {
                $this->user_analysis_graph_size_tb_height = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='user_communication_histry_view';");
        if ($results) {
            foreach ($results as $row) {
                $this->user_communication_histry_view = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='user_communication_list_view';");
        if ($results) {
            foreach ($results as $row) {
                $this->user_communication_list_view = $row['setting_value'];
            }
        }

        $results = $this->db_config->query("SELECT * FROM ms_setting WHERE setting_key='user_communication_view_conditions';");
        if ($results) {
            foreach ($results as $row) {
                $this->user_communication_view_conditions = $row['setting_value'];
            }
        }

    }

    // ゲッターメソッド
    public function getAccountLockoutCount()
    {
        return $this->account_lockout_count;
    }

    public function getMaintenance()
    {
        return $this->maintenance;
    }

    public function getSlackReportSendFlg()
    {
        return $this->slack_report_send_flg;
    }

    public function getSlackReportWebhookurl()
    {
        return $this->slack_report_webhookurl;
    }

    public function getSlackSendTest()
    {
        return $this->slack_send_test;
    }

    public function getLayout()
    {
        return $this->layout;
    }

    public function getAdminUserdataView()
    {
        return $this->admin_userdata_view;
    }

    public function getAdminNoticeView()
    {
        return $this->admin_notice_view;
    }

    public function getAdminNoticeInitialTitle()
    {
        return $this->admin_notice_initial_title;
    }

    public function getAdminNoticeInitialContent()
    {
        return $this->admin_notice_initial_content;
    }

    public function getUserInputHistoryView()
    {
        return $this->user_input_history_view;
    }

    public function getUserManagementView()
    {
        return $this->user_management_view;
    }

    public function getUserAnalysisGraphSizePcWidth()
    {
        return $this->user_analysis_graph_size_pc_width;
    }

    public function getUserAnalysisGraphSizePcHeight()
    {
        return $this->user_analysis_graph_size_pc_height;
    }

    public function getUserAnalysisGraphSizeSpWidth()
    {
        return $this->user_analysis_graph_size_sp_width;
    }

    public function getUserAnalysisGraphSizeSpHeight()
    {
        return $this->user_analysis_graph_size_sp_height;
    }

    public function getUserAnalysisGraphSizeTbWidth()
    {
        return $this->user_analysis_graph_size_tb_width;
    }

    public function getUserAnalysisGraphSizeTbHeight()
    {
        return $this->user_analysis_graph_size_tb_height;
    }

    public function getUserCommunicationHistryView()
    {
        return $this->user_communication_histry_view;
    }

    public function getUserCommunicationListView()
    {
        return $this->user_communication_list_view;
    }

    public function getUserCommunicationViewConditions()
    {
        return $this->user_communication_view_conditions;
    }
}
