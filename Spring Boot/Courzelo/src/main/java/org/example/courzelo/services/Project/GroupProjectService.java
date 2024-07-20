package org.example.courzelo.services.Project;



import org.example.courzelo.models.ProjectEntities.GroupProject;

import java.util.List;

public interface GroupProjectService {
    List<GroupProject> Getgroups();
    GroupProject getById(String id);
    GroupProject updategroup(GroupProject groupProject);
    void removegroup(String id);
    GroupProject addgroupAndAssignToproject(GroupProject groupProject, String id );
    void assignStudentsToGroup(String projectId);
    boolean isGroupAssignedToProject(String projectId);
    List<GroupProject> getProjectsForUser(String student_id);
}
