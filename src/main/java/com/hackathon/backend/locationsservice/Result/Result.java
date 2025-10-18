package com.hackathon.backend.locationsservice.Result;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.Base.BaseReadDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Result<TEntity,TDTO> {

    public final boolean isSuccess;
    public final Error error;

    public TEntity entity;
    public TDTO entityDTO;
    public List<TEntity> entities;
    public List<TDTO> entityDTOs;

    private Result(boolean isSuccess, Error error) {
        if ((isSuccess && error != Error.NONE) ||
                (!isSuccess && error == Error.NONE)) {
            throw new IllegalArgumentException("Invalid error: " + error);
        }

        this.isSuccess = isSuccess;
        this.error = error;
    }

    public static <TEntity, TDTO> Result<TEntity, TDTO> success() {
        return new Result<>(true, Error.NONE);
    }

    public static <TModel, TDTO> Result<TModel, TDTO> failure(Error error) {
        return new Result<>(false, error);
    }
}
