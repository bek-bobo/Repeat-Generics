package com.example.demo.core.coreModelMapper;

public abstract class CoreMapper<ENTITY, CREATE_VO, RESPONSE_VO, UPDATE_VO> {

    public abstract RESPONSE_VO convertToResponseVO(ENTITY entity);

    public abstract ENTITY fromCreateToEntity(CREATE_VO vo);

    public abstract ENTITY fromUpdateToEntity(UPDATE_VO vo,ENTITY entity);
}
