package com.br.zallpyws.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.query.internal.QueryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DAO {

  @PersistenceContext private EntityManager entityManager;
  @Value("${db.query.timeoutInSeconds}") public Integer queryTimeout;

  /* API */

  @Transactional
  public void save(Object entity) {
    Object id = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    if (id != null) {
      mergeLikePersist(entity);
    } else {
      entityManager.persist(entity);
    }
    entityManager.flush();
  }

  @Transactional
  public void remove(Object entity) {
    entityManager.remove(entity);
    entityManager.flush();
  }

  @Transactional
  public void update(String hql, Object... param) {
    fillParams(hql, param).executeUpdate();
    entityManager.flush();
  }

  @Transactional
  public void updateNative(String sql, Object... param) {
    fillParamsNative(sql, param).executeUpdate();
    entityManager.flush();
  }

  public void updateNoTransactionalNative(String sql, Object... param) {
    fillParamsNative(sql, param).executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.flush();
  }

  public List<?> list(String hql, Object... params) {
    return fillParams(hql, params).getResultList();
  }

  public List<?> listLimit(String hql, Integer max, Object... params) {
    Query query = fillParams(hql, params);
    query.setMaxResults(max);
    return query.getResultList();
  }

  public List<?> listFirstLimit(String hql, Integer first, Integer max, Object... params) {
    Query query = fillParams(hql, params);
    query.setFirstResult(first);
    query.setMaxResults(max);
    return query.getResultList();
  }

  public List<?> listNative(String sql, Object... params) {
    return fillParamsNative(sql, params).getResultList();
  }

  public List<?> listNativeByClass(String sql, Class<?> class_, Object... params) {
    return fillParamsNativeByClass(sql, class_, params).getResultList();
  }

  public List<?> listRootDistinct(final String hql, final Object... params) {
    final Query query = fillParams(hql, params);
    final QueryImpl<?> queryHib = ((QueryImpl<?>) query);
    queryHib.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
    return queryHib.list();
  }

  public Object single(String hql, Object... params) {
    try {
      return fillParams(hql, params).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  public Object first(String hql, Object... params) {
    try {
      final Query query = fillParams(hql, params);
      query.setMaxResults(1);
      return query.getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  public Object singleRootDistinct(final String hql, final Object... params) {
    final Query query = fillParams(hql, params);
    final QueryImpl<?> queryHib = ((QueryImpl<?>) query);
    queryHib.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
    return queryHib.uniqueResult();
  }

  /* PRIVATE */

  private Query fillParams(String hql, Object... param) {
    Query query = entityManager.createQuery(hql);
    query.setHint("org.hibernate.timeout", queryTimeout);
    if (hql.contains("?")) {
      for (int i = 0; i < param.length; i++)
        query.setParameter(i + 1, param[i]);
    } else {
      for (int i = 0; i < param.length; i = i + 2)
        query.setParameter((String) param[i], param[i + 1]);
    }
    return query;
  }

  private Query fillParamsNativeByClass(String sql, Class<?> class_, Object... param) {
    Query query;
    if (class_ != null) {
      query = entityManager.createNativeQuery(sql, class_);
    } else {
      query = entityManager.createNativeQuery(sql);
    }
    query.setHint("org.hibernate.timeout", queryTimeout);
    if (sql.contains("?")) {
      for (int i = 0; i < param.length; i++)
        query.setParameter(i + 1, param[i]);
    } else {
      for (int i = 0; i < param.length; i = i + 2)
        query.setParameter((String) param[i], param[i + 1]);
    }
    return query;
  }

  private Query fillParamsNative(String sql, Object... param) {
    Query query = entityManager.createNativeQuery(sql);
    query.setHint("org.hibernate.timeout", queryTimeout);
    if (sql.contains("?")) {
      for (int i = 0; i < param.length; i++)
        query.setParameter(i + 1, param[i]);
    } else {
      for (int i = 0; i < param.length; i = i + 2)
        query.setParameter((String) param[i], param[i + 1]);
    }
    return query;
  }

  private void mergeLikePersist(Object entity) {
    try {
      PropertyUtils.copyProperties(entity, entityManager.merge(entity));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Object singleNative(String sql, Object... params) {
    try {
      // Query query = fillParamsNative(sql, params);
      // final QueryImpl<?> queryHib = ((QueryImpl<?>) query);
      // return queryHib.uniqueResult();
      return fillParamsNative(sql, params).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  public Object singleNativeByClass(String sql, Class<?> class_, Object... params) {
    try {
      Query query = fillParamsNativeByClass(sql, class_, params);
      final QueryImpl<?> queryHib = ((QueryImpl<?>) query);
      return queryHib.uniqueResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  // controle de transação proprio:

  public void saveByEntityManager(Object entity, EntityManager em, Boolean commit, Boolean closeEm) {
    if (em == null || !em.isOpen() || em.getTransaction() == null) {
      throw new RuntimeException("EntityManager não estou ativo ou não foi iniciado");
    }
    if (!em.getTransaction().isActive()) {
      em.getTransaction().begin();
    }
    Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    if (id != null) {
      try {
        PropertyUtils.copyProperties(entity, em.merge(entity));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      em.persist(entity);
    }
    em.flush();
    if (commit) {
      em.getTransaction().commit();
      em.getTransaction().begin();
    }
    if (closeEm) {
      em.flush();
      em.close();
    }
  }

}