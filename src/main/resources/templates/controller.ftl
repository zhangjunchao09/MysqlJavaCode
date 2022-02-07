package ${pakage}.service.iml;

import ${pakage}.service.I${className}Service;
import ${pakage}.api.dto.${className}Dto;
import org.springframework.beans.factory.annotation.Autowired;
import com.zvos.common.base.result.GenericResultDto;
import com.zvos.common.base.result.GenericPageDTO;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class ${className}Controller {

    @Autowired
    I${className}Service ${lowclassName}Service;

    @RequestMapping(value = "/${lowclassName}.insert", method = RequestMethod.POST)
    public GenericResultDto insert${className}(@RequestBody ${className}Dto ${lowclassName}Dto) {
        if (null == ${lowclassName}Dto) {
            return GenericResultDto.fail("9999","参数为空");
        }
        ${lowclassName}Service.insert${className}(${lowclassName}Dto);
        return GenericResultDto.ok();
    }

    @RequestMapping(value = "/${lowclassName}.update$", method = RequestMethod.PUT)
    public GenericResultDto update${className}(@RequestBody ${className}Dto ${lowclassName}Dto) {
         if (null == ${lowclassName}Dto) {
             return GenericResultDto.fail("9999","参数为空");
         }
         ${lowclassName}Service.update${className}(${lowclassName}Dto);
         return GenericResultDto.ok();

    }

    @RequestMapping(value = "/${lowclassName}.delete", method = RequestMethod.DELETE)
    public GenericResultDto delete${className}(@RequestParam(value = "${primaryKey}") String ${primaryKey}) {
        if (null == ${primaryKey} || "".equals(${primaryKey})) {
             return GenericResultDto.fail("9999","参数为空");
        }
        ${lowclassName}Service.delete${className}(${primaryKey});

        return GenericResultDto.ok();
    }

    @RequestMapping(value = "/${lowclassName}.getByPk", method = RequestMethod.GET)
    public GenericResultDto<${className}Dto> get${className}(@RequestParam(value = "${primaryKey}") String ${primaryKey}) {
         if (null == ${primaryKey} || "".equals(${primaryKey})) {
             return GenericResultDto.fail("9999","参数为空");
         }
         ${className}Dto ${lowclassName}Dto = ${lowclassName}Service.get${className}(${primaryKey});
         return GenericResultDto.ok(${lowclassName}Dto);
    }

    @RequestMapping(value = "/${lowclassName}.get", method = RequestMethod.GET)
    public GenericResultDto<GenericPageDTO<${className}Dto>> get${className}s(@RequestBody ${className}Dto ${lowclassName}Dto) {
        List<${className}Dto> ${lowclassName}Dtos = ${lowclassName}Service.get${className}s(${lowclassName}Dto);

        GenericPageDTO<${className}Dto> pages = new GenericPageDTO<${className}Dto>();
        pages.setItems(${lowclassName}Dtos);
        pages.setTotal((long)${lowclassName}Dtos.size());

        return GenericResultDto.ok(pages);
    }

}
