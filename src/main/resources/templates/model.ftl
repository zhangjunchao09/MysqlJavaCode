package ${pakage}.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ${className}Model {

<#list typeMap?keys as key>
    /**
     * ${commentMap[key]}
     */
    private ${typeMap[key]} ${key};
</#list>

}
