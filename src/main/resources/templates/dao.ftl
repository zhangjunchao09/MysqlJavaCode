package ${pakage}.mapper;

import ${pakage}.model.${className}Model;
import ${pakage}.api.dto.${className}Dto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("${lowclassName}Mapper")
@Mapper
public interface ${className}Mapper {

    int insert${className}(${className}Model ${lowclassName}Model);

    int update${className}(${className}Model ${lowclassName}Model);

    int delete${className}(@Param("${primaryKey}") String ${primaryKey});

    ${className}Model get${className}(@Param("${primaryKey}") String ${primaryKey});

    List<${className}Model> get${className}s(${className}Dto ${lowclassName}Dto);

}