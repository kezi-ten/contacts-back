package com.contacts.mapper;

import com.contacts.pojo.Emp;
import com.contacts.pojo.EmpLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmpMapper {
    /**
     * 根据用户名和密码查询员工信息
     */
    @Select("select * from employee where emp_id = #{emp_id} and password = #{password}")
     Emp getUsernameAndPassword(Emp emp);

    /**
     * 查询所有的员工及其对应的部门名称
     */
    @Select("select * from employee")
    public List<Emp> list();

    int updateUserInfo(@Param("emp_id") String emp_id,
                       @Param("phone") String phone,
                       @Param("email") String email);

        //插入日志
        @Insert("insert into emp_log (operate_time, info) values (#{operateTime}, #{info})")
        public void insert(EmpLog empLog);

    @Insert("INSERT INTO employee (emp_id, name, phone, department_id, position, password, email) " +
            "VALUES (#{emp.emp_id}, #{emp.name}, #{emp.phone}, #{emp.department_id}, #{emp.position}, #{emp.password}, #{emp.email})")
    void insertemp(@Param("emp") Emp emp);

    @Delete("DELETE FROM employee WHERE emp_id = #{emp_id}")
    int deleteById(@Param("emp_id") String emp_id);

    @Update("UPDATE employee SET name = #{emp.name},  department_id = #{emp.department_id}, " +
            "position = #{emp.position}, password = #{emp.password} " +
            "WHERE emp_id = #{emp.emp_id}")
    int update(@Param("emp") Emp emp);

    @Update("UPDATE department SET supervisor_id = #{empId} WHERE name = #{departmentName}")
    int updateDepartmentSupervisor(@Param("empId") String empId,
                                   @Param("departmentName") String departmentName);

    @Update("UPDATE employee SET department_id = #{newDepartmentName}, position = '部门总管' WHERE emp_id = #{supervisor_id}")
    int updateEmployeeDepartmentAndPosition(@Param("supervisor_id") String supervisor_id,
                                            @Param("newDepartmentName")  String newDepartmentName);

}
