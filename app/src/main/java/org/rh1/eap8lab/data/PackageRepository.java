/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rh1.eap8lab.data;

import javax.enterprise.context.ApplicationScoped;
// import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.ArrayList;
import org.rh1.eap8lab.util.PackageItem;

import org.rh1.eap8lab.model.Package;

@ApplicationScoped
public class PackageRepository {
    @PersistenceContext
    private EntityManager em;

    public List<PackageItem> findAllBySubscriberid(Long subscriberid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<Package> pack = criteria.from(Package.class);
        criteria.multiselect(pack.get("id"), pack.get("amount")).where(cb.equal(pack.get("subscriberid"), subscriberid));
        List<Tuple> resultList = em.createQuery(criteria).getResultList();
        List<PackageItem> packageItems = new ArrayList<PackageItem>();
        resultList.forEach(tuple -> {
            System.out.printf("id: %s, amount: %s%n",
                    tuple.get(0, String.class), tuple.get(1, Long.class));
            PackageItem packageItem = new PackageItem();
            packageItem.id = tuple.get(0, String.class);
            packageItem.amount = tuple.get(1, Long.class);
            packageItems.add(packageItem);
        });
        return packageItems;
    }
}
