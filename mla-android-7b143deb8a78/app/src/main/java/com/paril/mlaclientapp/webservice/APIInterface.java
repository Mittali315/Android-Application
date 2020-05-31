package com.paril.mlaclientapp.webservice;
import com.paril.mlaclientapp.model.MLAAdminDetails;
import com.paril.mlaclientapp.model.MLAGradeTask;
import com.paril.mlaclientapp.model.MLAGroupDetails1;
import com.paril.mlaclientapp.model.MLAInstructorDetails;
import com.paril.mlaclientapp.model.MLARegisterUsers;
import com.paril.mlaclientapp.model.MLAScheduleDetailPostData;
import com.paril.mlaclientapp.model.MLAStudentDetails;
import com.paril.mlaclientapp.model.MLAStudentEnrollmentPostData;
import com.paril.mlaclientapp.model.MLASubjectDetails;
import com.paril.mlaclientapp.model.MLAFacebookDetails;
import com.paril.mlaclientapp.model.MLATaskDetails;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
public interface APIInterface {

    @GET("api/Register/GetRegisterAuth")
    Call<List<MLARegisterUsers>> authenticate(@Query("userName") String userName, @Query("password") String password);

    @GET("api/Register/GetRegisterByUserId")
    Call<List<MLARegisterUsers>> publicuser();

   // Call<List<MLARegisterUsers>> publicuser(@Query("userId") Integer userId, @Query("userName") String userName, @Query("userType") String userType,@Query("publicKey") String publicKey);

    @GET("api/Admin/GetAdminByUserName")
    Call<List<MLAAdminDetails>> getAdminInfo(@Query("userName") String userName);

    @GET("api/Student/GetStudentByUserName")
    Call<List<MLAStudentDetails>> getStudentInfo(@Query("userName") String userName);

    @GET("api/Instructor/GetInstructorByUserName")
    Call<List<MLAInstructorDetails>> getInstInfo(@Query("userName") String userName);

    @GET("api/Facebook/GetFacebookByUserName")
    Call<List<MLAFacebookDetails>> getFacebookInfo(@Query("userName") String userName);

    @GET("api/Admin/GetAllAdmin")
    Call<List<MLAAdminDetails>> getAdminUsers();

    @POST("api/DeleteAdmin/PostAdminRmv")
    Call<String> removeAdmin(@Query("idAdminRmv") String adminUserName);

    @POST("api/Register/PostAddAdmin")
    Call<MLAAdminDetails> addAdmin(@Query("adminUserName") String adminUserName, @Query("adminPassword") String adminPassword, @Query("adminFirsName") String adminFirsName, @Query("adminLastName") String adminLastName, @Query("adminTelephone") String adminTelephone, @Query("adminAddress") String adminAddress, @Query("adminAliasMailId") String adminAliasMailId, @Query("adminEmailId") String adminEmailId, @Query("adminSkypeId") String adminSkypeId);

    @POST("api/Admin/PostAdminUpdate")
    Call<String> updateAdmin(@Body MLAAdminDetails userDetails);

    @GET("api/Instructor/GetAllInstructor")
    Call<List<MLAInstructorDetails>> getInstructors();
 
//    @GET("api/Facebook/GetAllFacebook")
//    Call<List<MLAFacebookDetails>> getFacebook();

    @POST("api/DeleteInstructor/PostInstructorRmv")
    Call<String> removeInstructor(@Query("idInstructorRmv") String idInstructorRmv);

    @POST("api/Register/PostAddInstructor")
    Call<MLAInstructorDetails> addInst(@Query("instUserName") String userName, @Query("instPassword") String password, @Query("instFirsName") String instFirsName, @Query("instLastName") String instLastName, @Query("instTelephone") String instTelephone, @Query("instAddress") String instAddress, @Query("instAliasMailId") String instAliasMailId, @Query("instEmailId") String instEmailId, @Query("instSkypeId") String instSkypeId);

    @POST("api/Instructor/PostInstructorUpdate/")
    Call<String> updateInstructor(@Body MLAInstructorDetails userDetails);

    @GET("api/Student/GetAllStudent")
    Call<List<MLAStudentDetails>> getStudents();//1

    @GET("api/Subject/GetSubjectByStudent")
    Call<ArrayList<MLASubjectDetails>> getSubjForStudent(@Query("idStudent") String idStudent);

    @POST("api/DeleteStudent/PostStudentRmv")//2.
    Call<String> removeStudent(@Query("idStudentRmv") String idInstructorRmv);

    @POST("api/Register/PostAddStudent")//3.
    Call<MLAStudentDetails> addStudent(@Query("userName") String userName, @Query("password") String password, @Query("firsName") String instFirsName, @Query("lastName") String instLastName, @Query("telephone") String instTelephone, @Query("address") String instAddress, @Query("aliasMailId") String instAliasMailId, @Query("emailId") String instEmailId, @Query("skypeId") String instSkypeId , @Query("publicKey") String publicKey );

    @POST("api/Student/PostStudentUpdate/")//4.
    Call<String> updateStudent(@Body MLAStudentDetails userDetails);

    @GET("api/Facebook/GetAllFacebook")
    Call<List<MLAFacebookDetails>> getFacebook();//1

    @POST("api/Register/PostAddFacebookPost")
    Call<MLAFacebookDetails> addFacebook(@Query("postmsg") String face, @Query("sessionKey") String sessionKey,@Query("digitalSignature") String digitalSignature, @Query("idGroup") String idGroup );

    @GET("api/Groupkeytable/GetGroupkeytableById")
    Call<List<MLAGroupDetails1>> getAllGroupkey();

    @POST("api/Register/PostRegisterPassUpdate")
    Call<String> changePassword(@Query("userName") String userName, @Query("password") String password);

    @GET("api/Subject/GetAllSubject")
    Call<List<MLASubjectDetails>> getAllSubject();

    @GET("api/Subject/GetAllSubjectWithTask")
    Call<List<MLASubjectDetails>> getAllSubjectWithTask(@Query("flag") String subjectId);

    @POST("api/Subject/PostSubject")
    Call<MLASubjectDetails> addSubject(@Body MLASubjectDetails subjectDetails);

    @POST("api/Groupkeytable/PostGroupkeytable")
    Call<MLAGroupDetails1> addGroup(@Query("idGroup") String idGroup, @Query("version") String version, @Query("status") String status, @Query("groupKey") String groupKey ,@Query("userId") Integer userId) ;

    @POST("api/Grouptable/PostGrouptable")
    Call<MLAGroupDetails1> addGrouptable(@Query("idGroup") String idGroup, @Query("groupName") String groupName, @Query("userId") Integer userId, @Query("groupKey") String groupKey) ;

    @GET("api/Grouptable/GetAllGrouptable")
    Call<List<MLAGroupDetails1>> getAllGrouptable();

    @GET("api/Groupkeytable/GetAllGroup")
    Call<List<MLAGroupDetails1>> getAllGroup();

    @GET("api/Groupkeytable/GetAllGroup")
    Call<List<MLAGroupDetails1>> getAllGroupid();

    @POST("api/SubjectUpdate/PostSubjectUpdate")
    Call<String> updateSubject(@Body MLASubjectDetails subjectDetails);

    @POST("api/SubjectRmv/PostSubjectRmv")
    Call<String> removeSubject(@Query("subject_id") String idSubject);

    @GET("api/DeEnrollStudent/GetDeEnrollBySubject")
    Call<List<MLAStudentDetails>> getDeEnrollBySub(@Query("idSubject") String subjectId);


    @GET("api/DeEnrollStudent/GetDeEnrollByGroup")
    Call<List<MLAStudentDetails>> getDeEnrollByGroup(@Query("idGroup") String idGroup);

    @GET("api/EnrollStudent/GetEnrollBySubject")
    Call<List<MLAStudentDetails>> getEnrollBySub(@Query("idSubject") String subjectId);

    @POST("api/EnrollStudent/PostEnrollStudent")
    Call<MLAStudentEnrollmentPostData> enrollBySub(@Body MLAStudentEnrollmentPostData listStudentData);

    @POST("api/EnrollStudent/PostEnrollFriend")
    Call<MLAStudentEnrollmentPostData> enrollByFriend(@Body MLAStudentEnrollmentPostData listStudentData);


    @POST("api/DeEnrollStudent/PostDeEnroll")
    Call<MLAStudentEnrollmentPostData> deEnrollBySub(@Body MLAStudentEnrollmentPostData listStudentData);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET("api/EnrollStudent/GetSubjectByStd")
    Call<ArrayList<MLASubjectDetails>> getEnrolledSubjectForStudent(@Query("idStudent") String idStudent);

    @POST("api/Tasks/PostTask/")
    Call<String> addSchedule(@Body MLAScheduleDetailPostData details);


    @POST("api/ScheduleRmv/PostTaskRmv")
    Call<String> removeTasks(@Query("subject_id") String subjectId);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("api/Tasks/PostTaskUpdate")
    Call<String> updateTaskData(@Query("idTask") String idTask, @Query("topic") String topic, @Query("description") String desc);


    @GET("api/UserTasks/GetTasksByUser")
    Call<List<MLATaskDetails>> getTasksByUser(@Query("userId") String userName, @Query("userType") String userType);


    @GET("api/Tasks/GetTasksBySubject")
    Call<List<MLATaskDetails>> getTasksBySubject(@Query("subjectId") String subjectId);

    @GET("api/Tasks/GetStudentByTask")
    Call<List<MLAGradeTask>> getGrades(@Query("task") String task, @Query("subjectid") String subjId);

    @GET("api/Tasks/GetTasksByStudent")
    Call<List<MLATaskDetails>> getListTaskForStudent(@Query("subject") String subject, @Query("studentId") String studentId);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("api/Tasks/PostGradeUpdate")
    Call<String> updateGrade(@Query("task_id") String taskId, @Query("student_id") String student_id, @Query("grade") String grade);

    @GET("api/Tasks/GetTasksByStudent")
    Call<List<MLAGradeTask>> getGradesForStudent(@Query("studentId") String studentId, @Query("subject") String subject);

}