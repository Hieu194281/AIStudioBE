package vn.dasvision.template.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import vn.dasvision.template.api.requestbody.UpdateUserRoleRequest;
import vn.dasvision.template.api.requestbody.UserRoleRequest;
import vn.dasvision.template.config.untils.ConstantMessages;
import vn.dasvision.template.persistence.dto.UserRole;

import java.lang.reflect.Method;

//            System.out.println("");

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserRoleServiceTests {
    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private UserService userService;

    @Test
    void testUserRoleService() {
        try {
            /*
             * test getListUserRole
             * Map<String, Object> getListUserRole ( int pageSize, int currentPage, String keyword )
             * */
            System.out.println("=========Start test getListUserRole!!!!===============");
            Map<String, Object> resGetList = userRoleService.getListUserRole(0, 0, null);

            assertNotNull(resGetList, "Test get list user role successfully");

            Assert.notNull( userRoleService.getListUserRole(1, 1, null), "Test getUserRoleById successfully");
            Assert.notNull( userRoleService.getListUserRole(0,0, "k"), "Test getUserRoleById successfully");


//            Assert.notNull(resGetList, "Test get list user role successfully");
            System.out.println("=========End test getListUserRole!!!!===============");


//            System.out.println("=========End test getListUserRole!!!!===============");
//            System.out.println("=========End test getListUserRole!!!!===============");
//            System.out.println("=========End test getListUserRole!!!!===============");
//            System.out.println("=========End test getListUserRole!!!!===============");
//            System.out.println("[");
//            Method[] methods = userRoleService.getClass().getDeclaredMethods();
//            for (Method method : methods) {
//                System.out.println("'" +method.getName()+"' , ");
//            }
//            System.out.println("]");
//
//            System.out.println("=========End test getListUserRole!!!!===============");
//            System.out.println("=========End test getListUserRole!!!!===============");
//            System.out.println("=========End test getListUserRole!!!!===============");
//            System.out.println("=========End test getListUserRole!!!!===============");
//
//            Method[] methods2 = userService.getClass().getDeclaredMethods();
//            System.out.println("[");
//            for (Method method : methods2) {
//                System.out.println("'" +method.getName()+"'");
//
//            }
//            System.out.println("]");
//            System.out.println("=========End test getListUserRole!!!!===============");
//            System.out.println("=========End test getListUserRole!!!!===============");
//            System.out.println("=========End test getListUserRole!!!!===============");
//            System.out.println("=========End test getListUserRole!!!!===============");


//
//            System.out.println("=========Start test getListUserRole!!!!===============");
//            Map<String, Object> resGetList = userRoleService.getListUserRole(0, 0, null);
//
//            assertNotNull(resGetList, "Test get list user role successfully");
////            Assert.notNull(resGetList, "Test get list user role successfully");
//            System.out.println("=========End test getListUserRole!!!!===============");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            System.out.println("=========Start test userRoleService.getUserRoleById !!!!===============");

            System.out.println("id exit");
            Assert.notNull( userRoleService.getUserRoleById(0), "Test getUserRoleById successfully");
            System.out.println(userRoleService.getUserRoleById(0).toString());


            System.out.println("id not exit");
            Assert.notNull( userRoleService.getUserRoleById(1), "Test getUserRoleById successfully");
            System.out.println(userRoleService.getUserRoleById(1).toString());



            System.out.println("=========End test userRoleService.getUserRoleById !!!!===============");
            System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////");


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            System.out.println("=========Start test userRoleService.getListAllUserRole !!!!===============");
            Assert.notNull( userRoleService.getListAllUserRole(), "Test get getListAllUserRole successfully");
            System.out.println( userRoleService.getListAllUserRole().toString());

            System.out.println("=========End test userRoleService.getListAllUserRole !!!!===============");
            System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



            System.out.println("=========Start test userRoleService.addUserRole !!!!===============");
            Assert.notNull( userRoleService.addUserRole(new UserRoleRequest("vu", "vu", "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")), "Test addUserRole role successfully");
            Assert.notNull( userRoleService.addUserRole(new UserRoleRequest("", "vu", "iVBORw0KGgoAAAANSUhEUgAAAjQAAAE+CAYAAACa+Ir4AAAABHNCSVQICAgIfAhkiAAAABl0RVh0U29mdHdhcmUAZ25vbWUtc2NyZWVuc2hvdO8Dvz4AAABAaVRYdENyZWF0aW9uIFRpbWUAAAAAAFRo4bupIGhhaSwgMDUgVGjDoW5nIDEyIE7Eg20gMjAyMiAxMDozODo0MSArMDfDiU7yAAAcV0lEQVR4nO3df0zUd77v8Vevk3zrcjP22AyGZpjQHYdtENzDMlUvg00Z7bnSNgu0iZR2VZocBBs81nghJ11osxdtNghbQf7Qw70RMS3V7LpwopeTqJh0M+ZuM3NOd4t/3MXNniBpA5OQ8E3I9ptguH+Ayi+1LNLpZ3g+/uq8vzPf+cz0D5/5fmaGJ6ampqYEAABgsP+S6AUAAAAsF0EDAACMR9AAAADjETQAAMB4BA0AADAeQQMAAIxH0AAAAOMRNAAAwHgEDQAAMB5BAwAAjEfQAAAA4xE0AADAeAQNAAAwHkEDAACMR9AAAADjETQAAMB4BA0AADAeQQMAAIxH0AAAAOMRNAAAwHjJEzQTE")), "Test addUserRole role successfully");
            Assert.notNull( userRoleService.addUserRole(new UserRoleRequest(null, "vu", "")), "Test addUserRole role successfully");
            Assert.notNull( userRoleService.addUserRole(new UserRoleRequest(null, "vu", null)), "Test addUserRole role successfully");

//            Assert.notNull( userRoleService.addUserRole(new UserRoleRequest("admin", "vu", null)), "Test addUserRole role successfully");


            // exit role namw
            Assert.notNull( userRoleService.addUserRole(new UserRoleRequest("admin", "vu", null)), "Test addUserRole role successfully");
            Assert.notNull( userRoleService.addUserRole(new UserRoleRequest("a", "vu", null)), "Test addUserRole role successfully");


            System.out.println("=========End test userRoleService.addUserRole !!!!===============");
            System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            System.out.println("=========Start test userRoleService.updateUserRole !!!!===============");
            System.out.println("update 1");
            // ok
            Assert.isNull( userRoleService.updateUserRole(new UpdateUserRoleRequest("admin", "vu", null),"1").get("errors"), "Test updateUserRole role successfully");
            System.out.println("update 2");
            // nothing change
            Assert.notNull( userRoleService.updateUserRole(new UpdateUserRoleRequest(),"1"), "Test updateUserRole role successfully");
            Assert.notNull( userRoleService.updateUserRole(new UpdateUserRoleRequest("user", "vu", null),"1"), "Test updateUserRole role successfully");
            // error input
            Assert.notNull( userRoleService.updateUserRole(new UpdateUserRoleRequest("", "vu", "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"),""), "Test updateUserRole role successfully");
            Assert.notNull( userRoleService.updateUserRole(new UpdateUserRoleRequest(null, "vu", "iVBORw0KGgoAAAANSUhEUgAAAjQAAAE+CAYAAACa+Ir4AAAABHNCSVQICAgIfAhkiAAAABl0RVh0U29mdHdhcmUAZ25vbWUtc2NyZWVuc2hvdO8Dvz4AAABAaVRYdENyZWF0aW9uIFRpbWUAAAAAAFRo4bupIGhhaSwgMDUgVGjDoW5nIDEyIE7Eg20gMjAyMiAxMDozODo0MSArMDfDiU7yAAAcV0lEQVR4nO3df0zUd77v8Vevk3zrcjP22AyGZpjQHYdtENzDMlUvg00Z7bnSNgu0iZR2VZocBBs81nghJ11osxdtNghbQf7Qw70RMS3V7LpwopeTqJh0M+ZuM3NOd4t/3MXNniBpA5OQ8E3I9ptguH+Ayi+1LNLpZ3g+/uq8vzPf+cz0D5/5fmaGJ6ampqYEAABgsP+S6AUAAAAsF0EDAACMR9AAAADjETQAAMB4BA0AADAeQQMAAIxH0AAAAOMRNAAAwHgEDQAAMB5BAwAAjEfQAAAA4xE0AADAeAQNAAAwHkEDAACMR9AAAADjETQAAMB4BA0AADAeQQMAAIxH0AAAAOMRNAAAwHjJEzQTE"),"a"), "Test updateUserRole role successfully");
            Assert.notNull( userRoleService.updateUserRole(new UpdateUserRoleRequest(null, "vu", null),null), "Test updateUserRole role successfully");
            // id not exit
            Assert.notNull( userRoleService.updateUserRole(new UpdateUserRoleRequest("aaaa", "vu", null),"0"), "Test updateUserRole role successfully");


            //            Assert.isNull( userRoleService.updateUserRole(new UserRole(1, ConstantMessages.NOT_UPDATE_CHARACTER,  ConstantMessages.NOT_UPDATE_CHARACTER,  null, null,  ConstantMessages.NOT_UPDATE_CHARACTER,  1)).get("errors"), "Test updateUserRole role successfully");
//            System.out.println("update 3");
//            Assert.notNull( userRoleService.updateUserRole(new UserRole(3,"admin", "vu", null, null, null, 1)).get("errors"), "Test updateUserRole role successfully");

            System.out.println("=========End test userRoleService.updateUserRole !!!!===============");
            System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////");



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            System.out.println("=========Start test userRoleService.deleteUserRole !!!!===============");
            Assert.notNull( userRoleService.deleteUserRole(1).get("errors"), "Test get list user role successfully");
//            Assert.isNull(userRoleService.d);
            Assert.notNull( userRoleService.deleteUserRole(100).get("errors"), "Test get list user role successfully");

            System.out.println("=========End test userRoleService.deleteUserRole !!!!===============");
            System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        } catch (Exception e) {
            assertNull("Fail", "Test Fail");
        }

//        Map<String, Object> resGetList = userRoleService.getUserRoleById(1);
    }






}
