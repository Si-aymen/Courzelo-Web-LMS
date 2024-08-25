package org.example.courzelo.services;

import org.example.courzelo.dto.requests.GroupRequest;
import org.example.courzelo.dto.responses.GroupResponse;
import org.example.courzelo.dto.responses.PaginatedGroupsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface IGroupService {
    ResponseEntity<GroupResponse> getGroup(String groupID);
    ResponseEntity<PaginatedGroupsResponse> getGroupsByInstitution(String institutionID, int page, int sizePerPage);
    ResponseEntity<HttpStatus> createGroup(GroupRequest groupRequest);
    ResponseEntity<HttpStatus> updateGroup(String groupID, GroupRequest groupRequest);
    ResponseEntity<HttpStatus> deleteGroup(String groupID);
    ResponseEntity<HttpStatus> addStudentToGroup(String groupID, String studentID);
    ResponseEntity<HttpStatus> removeStudentFromGroup(String groupID, String studentID);

    void deleteGroupsByInstitution(String institutionID);
}
