/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.master;

import org.apache.hadoop.hbase.HBaseClassTestRule;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.testclassification.MasterTests;
import org.apache.hadoop.hbase.testclassification.MediumTests;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category({ MasterTests.class, MediumTests.class })
public class TestListTablesByState {
  @ClassRule
  public static final HBaseClassTestRule CLASS_RULE =
    HBaseClassTestRule.forClass(TestListTablesByState.class);

  private static HBaseTestingUtility UTIL;
  private static Admin ADMIN;
  private final static int SLAVES = 1;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    UTIL = new HBaseTestingUtility();
    UTIL.startMiniCluster(SLAVES);
    ADMIN = UTIL.getAdmin();
  }

  @Test
  public void testListTableNamesByState() throws Exception {
    TableName testTableName = TableName.valueOf("test");
    TableDescriptor testTableDesc = TableDescriptorBuilder.newBuilder(testTableName).build();
    ADMIN.createTable(testTableDesc);
    ADMIN.disableTable(testTableName);
    Assert.assertEquals(ADMIN.listTableNamesByState(false).get(0), testTableName);
    ADMIN.enableTable(testTableName);
    Assert.assertEquals(ADMIN.listTableNamesByState(true).get(0), testTableName);
  }

  @Test
  public void testListTableDescriptorByState() throws Exception {
    TableName testTableName = TableName.valueOf("test");
    TableDescriptor testTableDesc = TableDescriptorBuilder.newBuilder(testTableName).build();
    ADMIN.createTable(testTableDesc);
    ADMIN.disableTable(testTableName);
    Assert.assertEquals(ADMIN.listTableDescriptorsByState(false).get(0), testTableDesc);
    ADMIN.enableTable(testTableName);
    Assert.assertEquals(ADMIN.listTableDescriptorsByState(true).get(0), testTableDesc);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    if (ADMIN != null) {
      ADMIN.close();
    }
    if (UTIL != null) {
      UTIL.shutdownMiniCluster();
    }
  }
}