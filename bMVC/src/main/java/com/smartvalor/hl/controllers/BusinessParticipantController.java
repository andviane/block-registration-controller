package com.smartvalor.hl.controllers;

import com.smartvalor.db.BusinessEntity;
import com.smartvalor.db.BusinessEntityRepository;
import com.smartvalor.db.Persistence;
import com.smartvalor.hl.controllers.error.EntityNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@PropertySource("classpath:application.properties")
@ComponentScan(basePackageClasses = { Persistence.class, BusinessEntityRepository.class })
@Transactional
@RequestMapping(value = "/participants/business")
public class BusinessParticipantController {

    private static final Logger logger = Logger.getLogger(PersonController.class);

    @Autowired
    protected BusinessEntityRepository businessEntityRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public BusinessEntity[] getAll(@RequestParam(value = "size", required = false) Integer size,
                                   @RequestParam(value = "all", required = false) Boolean all,
                                   @RequestParam(value = "old", required = false) Boolean old) {

        boolean doOld = all || old;
        boolean doNew = all || !old;

        final List<BusinessEntity> collection = businessEntityRepository.findAll()
                .stream()
                .limit(size == null ? Long.MAX_VALUE : size)
                .filter(be -> (doNew && !be.isReviewed())
                        || (doOld && be.isReviewed()))
                .collect(Collectors.toList());

        return collection.toArray(new BusinessEntity[collection.size()]);
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public BusinessEntity getById(@PathVariable("uuid") UUID id) {

        final BusinessEntity entity = businessEntityRepository.findById(id);

        if (entity == null) {
            throw new EntityNotFoundException("Business entity wasn't found");
        }
        return entity;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BusinessEntity createNew(@RequestBody BusinessEntity entity) {

        final BusinessEntity saved = businessEntityRepository.save(entity);
        return saved;
    }
}
