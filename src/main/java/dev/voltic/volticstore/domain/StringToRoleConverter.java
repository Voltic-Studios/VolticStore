package dev.voltic.volticstore.domain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import dev.voltic.volticstore.repo.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(StringToRoleConverter.class);

    public StringToRoleConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role convert(String source) {
        logger.info("Converting role id: " + source);
        return roleRepository.findById(Long.parseLong(source)).orElse(null);
    }
}