package com.paxovision.db.util;

/**
 * Utilities for manage proxies.
 */
public class Proxies {

  public static final String BYTE_BUDDY_PATTERN = "$ByteBuddy$";

  /**
   * Check if class is proxified.
   *
   * @param clazz Class to check
   * @return True if class is proxified by CGLIB
   */
  public static boolean isProxified(Class clazz) {
    return clazz.getName().contains(BYTE_BUDDY_PATTERN);
  }

  /**
   * Return base of proxified class if it is proxified otherwise return class.
   *
   * @param clazz Clazz to evaluate
   * @return Class based of proxified
   */
  public static Class unProxy(Class clazz) {
    if (isProxified(clazz)) {
      return clazz.getSuperclass();
    }
    return clazz;
  }

}
