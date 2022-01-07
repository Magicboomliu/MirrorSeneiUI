import cv2
from pose import PoseDetector
import time
from utils.utils import draw_key_points, show_predefined_angles, draw_angles, StaticsJudge

if __name__ == "__main__":

    pose_model = PoseDetector(mode=False, min_detection_confidence=0.5, min_track_confidence=0.5)

    cap = cv2.VideoCapture('demo_res/demo.mp4')

    # Judge whether motion is static or dynamic
    static_mat = []
    statics_mode = False
    duration = 5
    record_id = 0

    pTime = 0
    while True:
        ret, frame = cap.read()
        if ret == True:
            # Get the results
            results, h, w = pose_model.get_the_landmark(frame)

            # Get pixel(2d), scene(3d) coordinate
            pixel_coord, scene_coord = pose_model.get_scene_and_pixel_coordinate(results, w, h)
            rendered_image = frame

            # judge  statics every 5 frames
            if record_id == duration:
                record_id = 0
                static_mat = []
            elif record_id == duration - 1:
                statics_mode = StaticsJudge(static_mat)
            record_id += 1
            static_mat.append(pixel_coord)

            # Show to Check whether the Static Judgement is work or not
            show_content = "Dynamic"
            if statics_mode == True:
                show_content = "Static"
            else:
                show_content = "Dynamic"
            cv2.putText(rendered_image, show_content, (70, 200), cv2.FONT_HERSHEY_PLAIN, 2,
                        (255, 0, 0), 2)

            # Show the FPS
            cTime = time.time()
            fps = 1 / (cTime - pTime)
            pTime = cTime

            cv2.putText(rendered_image, "FPS: " + str(int(fps)), (70, 100), cv2.FONT_HERSHEY_PLAIN, 3,
                        (255, 0, 0), 3)

            cv2.imshow("cap", rendered_image)
            if cv2.waitKey(5) & 0xff == ord('q'):
                break

    cap.release()
    cv2.destroyAllWindows()
