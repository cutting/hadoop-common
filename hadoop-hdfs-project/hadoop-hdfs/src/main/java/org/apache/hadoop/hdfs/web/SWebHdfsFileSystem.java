/**
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
package org.apache.hadoop.hdfs.web;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.io.Text;

public class SWebHdfsFileSystem extends WebHdfsFileSystem {

  public static final Text TOKEN_KIND = new Text("SWEBHDFS delegation");
  public static final String SCHEME = "swebhdfs";

  @Override
  public String getScheme() {
    return SCHEME;
  }

  @Override
  protected String getTransportScheme() {
    return "https";
  }

  @Override
  protected synchronized void initializeTokenAspect() {
    tokenAspect = new TokenAspect<WebHdfsFileSystem>(this, TOKEN_KIND);
  }

  @Override
  protected void initializeConnectionFactory(Configuration conf)
      throws IOException {
    connectionFactory = new URLConnectionFactory(
        URLConnectionFactory.DEFAULT_SOCKET_TIMEOUT);
    try {
      connectionFactory.setConnConfigurator(URLConnectionFactory
          .newSslConnConfigurator(URLConnectionFactory.DEFAULT_SOCKET_TIMEOUT,
              conf));
    } catch (GeneralSecurityException e) {
      throw new IOException(e);
    }
  }

  @Override
  protected int getDefaultPort() {
    return getConf().getInt(DFSConfigKeys.DFS_NAMENODE_HTTPS_PORT_KEY,
        DFSConfigKeys.DFS_NAMENODE_HTTPS_PORT_DEFAULT);
  }
}
