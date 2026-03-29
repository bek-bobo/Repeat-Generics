package com.example.demo.core.service;


import com.example.demo.core.coreModelMapper.CoreMapper;
import com.example.demo.core.repository.CoreRepository;
import com.example.demo.rsql.SpecificationBuilder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;


public abstract class CoreService<ID, ENTITY, RESPONSE_VO, CREATE_VO, UPDATE_VO> {

    protected abstract CoreRepository<ENTITY, ID> getRepository();

    protected abstract Class<ENTITY> getEntityClass();

    protected abstract CoreMapper<ENTITY, CREATE_VO, RESPONSE_VO, UPDATE_VO> getCoreMapper();

    protected abstract RESPONSE_VO internalCreate(CREATE_VO createVo);

    protected abstract RESPONSE_VO internalUpdate(ID id, UPDATE_VO updateVo);

    public RESPONSE_VO create(CREATE_VO createVo) {
        return internalCreate(createVo);
    }

    @Transactional(readOnly = true)
    public Page<RESPONSE_VO> getAll(String predicate, Pageable pageable) {
        Specification<ENTITY> specification = SpecificationBuilder.build(predicate);

        Page<ENTITY> pages;

        if (specification == null) {
            pages = getRepository().findAll(pageable);
        } else {
            pages = getRepository().findAll(specification, pageable);
        }

        return pages.map(entity -> getCoreMapper().convertToResponseVO(entity));
    }

    @Transactional(readOnly = true)
    public RESPONSE_VO getById(ID id) {

        return getCoreMapper().convertToResponseVO
                (getRepository()
                        .findById(id)
                        .orElseThrow(
                                () -> notFoundException(id)));


    }

    @Transactional
    public RESPONSE_VO update(ID id, UPDATE_VO updateVo) {
        return internalUpdate(id, updateVo);
    }


    @Transactional
    public void delete(ID id) {
        ENTITY entity = getRepository().findById(id)
                .orElseThrow(() -> notFoundException(id));

        getRepository().delete(entity);
    }


    protected abstract RuntimeException notFoundException(ID id);


}
