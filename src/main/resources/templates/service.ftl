package ${pakage}.service;

import ${pakage}.api.dto.${className}Dto;

import java.util.List;

public interface I${className}Service {

    boolean insert${className}(${className}Dto ${lowclassName}Dto);

    boolean update${className}(${className}Dto ${lowclassName}Dto);

    boolean delete${className}(String ${primaryKey});

    ${className}Dto get${className}(String ${primaryKey});

    List<${className}Dto> get${className}s(${className}Dto ${lowclassName}Dto);

}
