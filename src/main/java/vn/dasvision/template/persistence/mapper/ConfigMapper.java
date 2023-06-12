package vn.dasvision.template.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import vn.dasvision.template.persistence.dto.Config;

import java.util.Map;

@Mapper
public interface ConfigMapper {
    Config getConfig() throws Exception;
}
