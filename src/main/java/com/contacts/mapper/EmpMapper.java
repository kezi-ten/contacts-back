package com.contacts.mapper;

import com.contacts.pojo.Emp;
import com.contacts.pojo.EmpLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

}
