package com.example.demo.Service;

import com.example.demo.DTO.SourceFileWithCount;
import com.example.demo.PO.Group;
import com.example.demo.PO.SourceFile;
import com.example.demo.PO.User;
import com.example.demo.DTO.RecordDTO;

import java.util.List;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-25 17:05
 */
public interface UserService {
    public int register(String name, String password);

    public User login(String name, String password);

    public List<SourceFile> getUserSources(int userId);

    public boolean addUserSource(String fileId, int userId, String sourceName, String uploadTime, String type);

    public boolean shareSource(int groupId, String sourceName);

    public Group addGroup(String name, String description);

    public boolean addGroupMember(int groupId, int userId);

    public List<Group> getUserGroups(int userId);

    public List<User> getGroupUsers(int groupId);

    public List<SourceFile> getGroupSources(int groupId);

    public boolean addRecord(int userId,String sourceId,String targetId, String time);

    public List<RecordDTO> getUserHistory(int userId);

    public List<SourceFileWithCount> getMostUsedUserSources(int userId);

    public List<SourceFileWithCount> getMostUsedUserGroupSources(int userId);

    public List<SourceFile> getGroupSourceFileByUserId(int userId);

    public boolean updateRecord(String targetId, String time);
}