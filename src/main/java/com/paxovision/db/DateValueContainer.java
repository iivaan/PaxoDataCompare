package com.paxovision.db;

import com.paxovision.db.lettercase.*;
import com.paxovision.db.util.*;
/**
 * This interface indicates container of {@link DateValue}.
 *
 */
public interface DateValueContainer {

  /**
   * Returns the date.
   * 
   * @return The date.
   */
  DateValue getDate();

  /**
   * Returns if it is midnight.
   * @return {@code true} if it is midnight.
   */
  boolean isMidnight();

}
