/*******************************************************************************
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     02/04/2013-2.5 Guy Pelletier 
 *       - 389090: JPA 2.1 DDL Generation Support
 ******************************************************************************/ 
package org.eclipse.persistence.testing.models.jpa21.advanced.ddl.schema;

import static javax.persistence.GenerationType.TABLE;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="JPA21_DDL_ORGANIZER", schema="PERSON")
public class Organizer {
    @Id
    @GeneratedValue(strategy=TABLE, generator="JPA21_ORGANIZER_GENERATOR")
    @TableGenerator(
        name="JPA21_ORGANIZER_GENERATOR", 
        table="SCHEMA_PK_SEQ",
        schema="GENERATOR",
        pkColumnName="SEQ_NAME", 
        valueColumnName="SEQ_COUNT",
        pkColumnValue="ORGANIZER_SEQ"
    )
    public Integer id;
    public String name;
    
    @ManyToOne
    @JoinColumn(
        name="RACE_ID",
        foreignKey=@ForeignKey(
            name="Organizer_Race_Foreign_Key",
            foreignKeyDefinition="FOREIGN KEY (RACE_ID) REFERENCES JPA21_DDL_RACE (ID)"      
        )
    )
    public Race race;
    
    public Organizer() {}
    
    public Integer getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public Race getRace() {
        return race;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
