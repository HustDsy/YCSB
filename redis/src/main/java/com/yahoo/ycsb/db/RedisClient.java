/**
 * Copyright (c) 2012 YCSB contributors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */

/**
 * Redis client binding for YCSB.
 *
 * All YCSB records are mapped to a Redis *hash field*.  For scanning
 * operations, all keys are saved (by an arbitrary hash) in a sorted set.
 */

package com.yahoo.ycsb.db;

import com.yahoo.ycsb.ByteIterator;
import com.yahoo.ycsb.DB;
import com.yahoo.ycsb.DBException;
import com.yahoo.ycsb.Status;
//import com.yahoo.ycsb.StringByteIterator;
import redis.clients.jedis.BasicCommands;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.Protocol;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
/*import java.util.Iterator;
import java.util.List;*/
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

//////////////meggie
import java.io.*;
import com.yahoo.ycsb.WorkloadsWriteFile;
//////////////meggie

/**
 * YCSB binding for <a href="http://redis.io/">Redis</a>.
 *
 * See {@code redis/README.md} for details.
 */
public class RedisClient extends DB {

  private JedisCommands jedis;
  //////////meggie
  //private OutputStream out;
  private FileWriter out;
  private FileWriter outread;
  //private Writer writer;
  //////////meggie

  public static final String HOST_PROPERTY = "redis.host";
  public static final String PORT_PROPERTY = "redis.port";
  public static final String PASSWORD_PROPERTY = "redis.password";
  public static final String CLUSTER_PROPERTY = "redis.cluster";

  public static final String INDEX_KEY = "_indices";

  public void init() throws DBException {
    Properties props = getProperties();
    int port;

    String portString = props.getProperty(PORT_PROPERTY);
    if (portString != null) {
      port = Integer.parseInt(portString);
    } else {
      port = Protocol.DEFAULT_PORT;
    }
    String host = props.getProperty(HOST_PROPERTY);

    /////////////meggie
    File f = null, fread = null;
    out = null;
    //outread = null;
    //File pf = null;
    //writer = null;
    try{
      // f = new File("/home/cxy/workloads/zipfian/write.txt");
      f = new File("/Users/dongshenyu/Desktop/write.txt");
      // f = new File("/home/cxy/workloads/uniform/write.txt");
      // f = new File("/tmp/write.txt");
      //fread = new File("/mnt/workloads/zipfian/read.txt");
      //out = new FileOutputStream(f, true);
      out = new FileWriter(f, true);
      //outread = new FileWriter(fread, true);
      //pf =new File("/home/meggie/Documents/print.txt");
      //writer = new FileWriter(pf, true);
    }catch(Exception e){
      e.printStackTrace();
    }
    /////////////meggie
 
    boolean clusterEnabled = Boolean.parseBoolean(props.getProperty(CLUSTER_PROPERTY));
    if (clusterEnabled) {
      Set<HostAndPort> jedisClusterNodes = new HashSet<>();
      jedisClusterNodes.add(new HostAndPort(host, port));
      jedis = new JedisCluster(jedisClusterNodes);
    } else {
      jedis = new Jedis(host, port);
      ((Jedis) jedis).connect();
    }

    String password = props.getProperty(PASSWORD_PROPERTY);
    if (password != null) {
      ((BasicCommands) jedis).auth(password);
    }
  }

  public void cleanup() throws DBException {
    try {
      ((Closeable) jedis).close();
    } catch (IOException e) {
      throw new DBException("Closing connection failed.");
    }
  }

  /*
   * Calculate a hash for a key to store it in an index. The actual return value
   * of this function is not interesting -- it primarily needs to be fast and
   * scattered along the whole space of doubles. In a real world scenario one
   * would probably use the ASCII values of the keys.
   */
  private double hash(String key) {
    return key.hashCode();
  }

  // XXX jedis.select(int index) to switch to `table`

  @Override
  public Status read(String table, String key, Set<String> fields,
      Map<String, ByteIterator> result) {
    if (fields == null) {
      //StringByteIterator.putAllAsByteIterators(result, jedis.hgetAll(key));
      ////////////meggie
      try{
        String value = "";
        WorkloadsWriteFile.appendWorkloadToFile1(out, WorkloadsWriteFile.READ, key, value, null);    
        //writer.write("read, key:" + key);
        //writer.write("\r\n");
        //writer.flush();
      }catch(Exception e){
        e.printStackTrace();
      }
      ////////////meggie
    } else {
      /*List<String> values = jedis.hmget(key, fieldArray);
      Iterator<String> fieldIterator = fields.iterator();
      Iterator<String> valueIterator = values.iterator();
      while (fieldIterator.hasNext() && valueIterator.hasNext()) {
        result.put(fieldIterator.next(),
            new StringByteIterator(valueIterator.next()));
      }
      assert !fieldIterator.hasNext() && !valueIterator.hasNext();*/
    }
    return result.isEmpty() ? Status.ERROR : Status.OK;
  }

  @Override
  public Status insert(String table, String key,
      Map<String, ByteIterator> values) {
    ///////////////meggie
    try{
      for(Map.Entry<String, ByteIterator> entry : values.entrySet()){
        String value = entry.getValue().toString();
        WorkloadsWriteFile.appendWorkloadToFile1(out, WorkloadsWriteFile.INSERT, key, value, null); 
        //writer.write("insert, key:" + key + ", value:" + value);
      }
      //writer.write("\r\n");
      //writer.flush();
    }catch(Exception e){
      e.printStackTrace();
    }
    ///////////////meggie
    /*if (jedis.hmset(key, StringByteIterator.getStringMap(values))
        .equals("OK")) {
      jedis.zadd(INDEX_KEY, hash(key), key);
      return Status.OK;
    }
    return Status.ERROR;*/
    return Status.OK;
  }

  @Override
  public Status delete(String table, String key) {
    ///////////////meggie
    String value = "";
    WorkloadsWriteFile.appendWorkloadToFile1(out, WorkloadsWriteFile.DELETE, 
        key, value, null);    
    ///////////////meggie
    
    /*
    return jedis.del(key) == 0 && jedis.zrem(INDEX_KEY, key) == 0 ? Status.ERROR
        : Status.OK;*/
    return Status.OK;
  }

  @Override
  public Status update(String table, String key,
      Map<String, ByteIterator> values) {
    ///////////////meggie
    try{
      for(Map.Entry<String, ByteIterator> entry : values.entrySet()){
        String value = entry.getValue().toString();
        WorkloadsWriteFile.appendWorkloadToFile1(out, WorkloadsWriteFile.UPDATE, 
            key, value, null);    
        //writer.write("update, key:" + key + ", value:" + value);
      }
      //writer.write("\r\n");
      //writer.flush();
    }catch(Exception e){
      e.printStackTrace();
    }
    ///////////////meggie
    /*return jedis.hmset(key, StringByteIterator.getStringMap(values))
        .equals("OK") ? Status.OK : Status.ERROR;*/
    return Status.OK;
  }

  @Override
  public Status scan(String table, String startkey, int recordcount,
      Set<String> fields, Vector<HashMap<String, ByteIterator>> result) {
    String value = "";
    WorkloadsWriteFile.appendWorkloadToFile1(out, WorkloadsWriteFile.SCAN, 
            startkey, value, null); 
    //Set<String> keys = jedis.zrangeByScore(INDEX_KEY, hash(startkey),
      //  Double.POSITIVE_INFINITY, 0, recordcount);
    //HashMap<String, ByteIterator> values;
    //for (String key : keys) {
      //values = new HashMap<String, ByteIterator>();
      //read(table, key, fields, values);
      //result.add(values);
    //}

    return Status.OK;
  }

}
