<?php

namespace Domain\Services;

use Domain\Models\GroupListRepository;
use Domain\Models\GroupSettingRepository;

class GroupRepositoryCollection
{
    private GroupListRepository $groupListRepository;
    private GroupSettingRepository $groupSettingRepository;

    public function __construct(
        GroupListRepository    $groupListRepository,
        GroupSettingRepository $groupSettingRepository
    )
    {
        $this->groupListRepository = $groupListRepository;
        $this->groupSettingRepository = $groupSettingRepository;
    }

    public function refer()
    {
        $group_list_refer = $this->groupListRepository->refer();
        $group_setting_refer = $this->groupSettingRepository->refer();
        return [$group_list_refer, $group_setting_refer];
    }

    public function exists()
    {
        $group_list_refer = $this->groupListRepository->refer();
        $group_setting_refer = $this->groupSettingRepository->refer();
        if ($group_list_refer and $group_setting_refer) {
            return True;
        }
        return False;
    }

    public function entry()
    {
        $this->groupListRepository->entry();
        $this->groupSettingRepository->entry();
    }

    public function delete()
    {
        $this->groupListRepository->delete();
        $this->groupSettingRepository->delete();
    }
}
