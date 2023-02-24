package vn.amisoft.dao.entities;

import vn.amisoft.common.models.BaseModel;

public abstract class BaseEntity<T extends BaseModel> {
    public abstract T toModel();
    public abstract void ofModel(T t);
}
