<?php

namespace Application\Services;

use Domain\Services\GroupRepositoryCollection;

class GroupManagementService
{
    private GroupRepositoryCollection $groupRepositoryCollection;

    public function __construct(GroupRepositoryCollection $groupRepositoryCollection)
    {
        $this->groupRepositoryCollection = $groupRepositoryCollection;
    }

    public function certification($groups_id)
    {
        foreach (INVALID_GROUPS_ID as $char) {
            if (strpos($groups_id, $char) !== false) {
                return "unavailable group id";
            }
        }

        $result = $this->groupRepositoryCollection->exists();
        if ($result) {
            return "group id is duplication";
        }
        return "success";
    }

    public function entryGroup($groups_id)
    {
        $certification_result = $this->certification($groups_id);

        if ($certification_result == 'unavailable group id') {
            return $certification_result;
        } else if ($certification_result == 'group id is duplication') {
            return $certification_result;
        } else {
            $this->groupRepositoryCollection->entry();
            return $certification_result;
        }
    }

    public function deleteGroup($groups_id)
    {
        $result = $this->groupRepositoryCollection->exists();
        if ($result) {
            $this->groupRepositoryCollection->delete();
            return True;
        }
        return False;

    }
}
