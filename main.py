# all libraries
import pyautogui
import schedule
import time
import cv2
import numpy as np
import mysql.connector

# Disclaimers
print("Requirements: (Python version > 3.0) and ('schedule', 'pyautogui', 'mysql-connector-python' packages installed)")
print('You can exit this program using (Ctrl+c) at any time')

# DATABASE CONNECTION

# Database connection details
db_host = "localhost"
db_user = "anand_sql"
db_password = "yash1122"
db_database = "regumate"


def get_meeting_credentials():
    try:
        con = mysql.connector.connect(
            host=db_host,
            user=db_user,
            password=db_password,
            database=db_database
        )

        cursor = con.cursor()

        cursor.execute("SELECT meeting_id, passcode, meeting_time, total_meeting FROM meetings")
        result = cursor.fetchone()

        if result:
            meet_id = result[0]
            password = result[1]
            meet_time = result[2]
            total_meet = result[3]
            return meet_id, password, meet_time, total_meet
        else:
            print("No meeting credentials found.")
            return None

    except mysql.connector.Error as e:
        print(f"Error: {e}")
    finally:
        if con.is_connected():
            cursor.close()
            con.close()

# this is the moin and important function
def zoomClass(meet_id, password, meet_time, total_meet):
    time.sleep(0.2)

    pyautogui.press('esc', interval=0.1)
    time.sleep(0.3)

    pyautogui.press('win', interval=0.5)
    pyautogui.write('zoom')
    time.sleep(2)
    pyautogui.press('enter', interval=0.5)
    time.sleep(10)
    pyautogui.hotkey('win', 'up')

    # importing, loading, and matching of the first image named joinIMG.png, which is the join button in the Zoom app
    screenshot = pyautogui.screenshot()
    screenshot_cv = cv2.cvtColor(np.array(screenshot), cv2.COLOR_RGB2BGR)
    template_join = cv2.imread('/home/anand/anand/zoom/joinIMG.png')
    result_join = cv2.matchTemplate(screenshot_cv, template_join, cv2.TM_CCOEFF_NORMED)
    min_val_join, max_val_join, min_loc_join, max_loc_join = cv2.minMaxLoc(result_join)
    threshold_join = 0.8

    if max_val_join >= threshold_join:
        # know the center coordinates of the bounding box
        w_join, h_join = template_join.shape[:-1]
        center_x_join, center_y_join = max_loc_join[0] + w_join // 2, max_loc_join[1] + h_join // 2

        # Click at the center of the bounding box for "joinIMG"
        pyautogui.click(center_x_join, center_y_join)
        time.sleep(5)

        # importing, loading, and matching of the second image named meetidimage.png, which is the write meeting ID button in the Zoom app
        screenshot_after_join = pyautogui.screenshot()
        screenshot_after_join_cv = cv2.cvtColor(np.array(screenshot_after_join), cv2.COLOR_RGB2BGR)
        template_after_join = cv2.imread('/home/anand/anand/zoom/meetidimage.png')
        result_after_join = cv2.matchTemplate(screenshot_after_join_cv, template_after_join, cv2.TM_CCOEFF_NORMED)
        min_val_after_join, max_val_after_join, min_loc_after_join, max_loc_after_join = cv2.minMaxLoc(result_after_join)
        threshold_after_join = 0.9

        if max_val_after_join >= threshold_after_join:
            # know the center coordinates of the bounding box
            w_after_join, h_after_join = template_after_join.shape[:-1]
            center_x_after_join, center_y_after_join = max_loc_after_join[0] + w_after_join // 2, max_loc_after_join[1] + h_after_join // 2

            # operations on the second image (writing meeting ID and writing password and pressing enter)
            pyautogui.moveTo(center_x_after_join, center_y_after_join)
            pyautogui.click(center_x_after_join, center_y_after_join)
            pyautogui.write(meet_id)
            pyautogui.press('enter', interval=5)
            pyautogui.write(password)
            pyautogui.press('enter', interval=5)

            time.sleep(total_meet * 60)
            pyautogui.hotkey('alt', 'f4')
            pyautogui.press('enter', interval=3)

        else:
            print("meetidimage image not found on the screen after joining.")
    else:
        print("Join image not found on the screen.")


credentials = get_meeting_credentials()
if credentials:
    meet_id, password, meet_time, total_meet = credentials
    schedule.every().day.at("%s" % meet_time).do(zoomClass, meet_id=meet_id, password=password, meet_time=meet_time, total_meet=total_meet)
    print("Scheduling everyday at ", meet_time)

while True:
    # to Check whether a scheduled task is pending to run or not
    schedule.run_pending()
    time.sleep(1)

