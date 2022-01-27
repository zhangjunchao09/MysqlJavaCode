<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<#macro mapperEl field type>${r"#{"}${field}${r",jdbcType="}${type}}</#macro>

<mapper namespace="${pakage}.mapper.${className}Mapper">

    <insert id="insert${className}" parameterType="${pakage}.model.${className}Model">
        <selectKey keyProperty="${primaryKey}" resultType="java.lang.Integer">
                select LAST_INSERT_ID()
        </selectKey>
        insert into ${tableName} (
        <#list fieldMap?keys as key>
            <#if key != "${primaryKeyField}">
            ${key}<#if key_has_next>,</#if>
            </#if>
        </#list>
        )
        values (
        <#list fieldMap?keys as key>
            <#if key != "${primaryKeyField}">
            <@mapperEl fieldMap[key] typeMap[key]/><#if key_has_next>,</#if>
            </#if>
        </#list>
        )
    </insert>

    <select id="get${className}" resultType="${pakage}.model.${className}Model">
        SELECT
        <#list fieldMap?keys as key>
        ${key} as ${fieldMap[key]}<#if key_has_next>,</#if>
        </#list>
        FROM ${tableName}
        where ${primaryKeyField} = ${r"#{"}${primaryKey}}
    </select>

    <select id="get${className}s" resultType="${pakage}.model.${className}Model" parameterType="${pakage}.api.dto.${className}Dto">
        SELECT
        <#list fieldMap?keys as key>
                ${key} as  ${fieldMap[key]} <#if key_has_next>,</#if>
        </#list>
        FROM ${tableName}
        <where>
           <#list fieldMap?keys as key>
              <#if key != "${primaryKeyField}">
              <if test="${fieldMap[key]} != null" >
                  and ${key} = <@mapperEl fieldMap[key] typeMap[key]/>
              </if>
              </#if>
            </#list>
        </where>
    </select>

    <update id="update${className}" parameterType="${pakage}.model.${className}Model">
        update ${tableName}
        <set>
        <#list fieldMap?keys as key>
            <#if key != "${primaryKeyField}">
            <if test="${fieldMap[key]} != null" >
                ${key} = <@mapperEl fieldMap[key] typeMap[key]/><#if key_has_next>,</#if>
            </if>
            </#if>
        </#list>
        </set>
        where ${primaryKeyField} = ${r"#{"}${primaryKey}}
    </update>

    <delete id="delete${className}" >
        delete from ${tableName} where ${primaryKeyField} = ${r"#{"}${primaryKey}}
    </delete>
</mapper>
