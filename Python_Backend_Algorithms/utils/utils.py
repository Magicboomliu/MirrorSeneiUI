from typing import List
import cv2
# import sys

# sys.path.append("../")
# from pose import PoseDetector
import numpy as np
import math


def get_2d(pixel_coord_instance):
    # u, v
    return pixel_coord_instance[1:-1]


def get_3d(scene_coord_instance):
    # x, y, z
    return scene_coord_instance[1:-1]


def draw_line_2d(image, coord, idx1, idx2, color=(0, 255, 0), thickness=2):
    if coord[idx1][-1] > 0.4 and coord[idx2][-1] > 0.4:
        cv2.line(image, tuple(get_2d(coord[idx1])), tuple(get_2d(coord[idx2])), color, thickness=thickness)


def draw_key_points(image, pixel_2d, draw_line=False, skip_face=True):
    if len(pixel_2d) == 0:
        return image

    for idx, pixels in enumerate(pixel_2d):
        # Skip Face
        if skip_face:
            if idx in [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]:
                continue
        u, v = get_2d(pixels)
        # Draw circles
        cv2.circle(image, (u, v), 5, (255, 0, 0), 2)
        # Draw Lines

    # draw lines
    if draw_line:
        # Left hand
        draw_line_2d(image, pixel_2d, 20, 18)
        draw_line_2d(image, pixel_2d, 18, 16)
        draw_line_2d(image, pixel_2d, 16, 22)
        draw_line_2d(image, pixel_2d, 20, 16)

        # Left Small Arm
        draw_line_2d(image, pixel_2d, 16, 14)

        # Left Big ARM
        draw_line_2d(image, pixel_2d, 14, 12)

        # Right Hand
        draw_line_2d(image, pixel_2d, 21, 15)
        draw_line_2d(image, pixel_2d, 15, 19)
        draw_line_2d(image, pixel_2d, 19, 17)
        draw_line_2d(image, pixel_2d, 15, 17)

        # Right Small Arm
        draw_line_2d(image, pixel_2d, 15, 13)

        # Right Big ArM
        draw_line_2d(image, pixel_2d, 11, 13)

        # Chest
        draw_line_2d(image, pixel_2d, 12, 11)
        draw_line_2d(image, pixel_2d, 11, 23)
        draw_line_2d(image, pixel_2d, 23, 24)
        draw_line_2d(image, pixel_2d, 12, 24)

        # Left Big Leg
        draw_line_2d(image, pixel_2d, 24, 26)

        # Left small Leg
        draw_line_2d(image, pixel_2d, 26, 28)

        # Right Big Leg
        draw_line_2d(image, pixel_2d, 23, 25)

        # Right Small Leg
        draw_line_2d(image, pixel_2d, 25, 27)

        # Left Foot
        draw_line_2d(image, pixel_2d, 28, 32)
        draw_line_2d(image, pixel_2d, 32, 30)
        draw_line_2d(image, pixel_2d, 28, 30)

        # Right Foot
        draw_line_2d(image, pixel_2d, 27, 29)
        draw_line_2d(image, pixel_2d, 27, 31)
        draw_line_2d(image, pixel_2d, 29, 31)

    return image


def angle_computation(p1, p2, p3):
    """
    calculate the angle between the vector (p1p2) and (p2p3)

    """

    p1 = np.array(get_3d(p1))

    p2 = np.array(get_3d(p2))

    p3 = np.array(get_3d(p3))

    f1 = p2 - p1
    f2 = p2 - p3

    p1p2_norm = f1 / np.linalg.norm(f1)
    p2p3_norm = f2 / np.linalg.norm(f2)

    res = np.dot(p1p2_norm, p2p3_norm)

    # angle = np.math.acos(res) * 180 / np.math.pi
    angle = np.arccos(res) * 180 / np.pi
    angle = round(angle, 2)

    return angle


# give scene coord and part name, return angle
def show_predefined_angles(p3d, mode='elbow_left'):
    angle = 0
    if len(p3d) == 0:
        return
    if mode == 'elbow_left':
        angle = angle_computation(p3d[12], p3d[14], p3d[16])
    elif mode == 'elbow_right':
        angle = angle_computation(p3d[15], p3d[13], p3d[11])
    elif mode == 'chest_upper_left':
        angle = angle_computation(p3d[11], p3d[12], p3d[24])
    elif mode == 'chest_upper_right':
        angle = angle_computation(p3d[12], p3d[11], p3d[23])
    elif mode == 'chest_bottom_left':
        angle = angle_computation(p3d[12], p3d[24], p3d[23])
    elif mode == 'chest_bottom_right':
        angle = angle_computation(p3d[11], p3d[23], p3d[24])
    elif mode == 'left_big_leg_angle':
        angle = angle_computation(p3d[23], p3d[24], p3d[26])
    elif mode == 'right_big_leg_angle':
        angle = angle_computation(p3d[24], p3d[23], p3d[25])
    elif mode == 'left_knee_angle':
        angle = angle_computation(p3d[24], p3d[26], p3d[28])
    elif mode == 'right_knee_angle':
        angle = angle_computation(p3d[23], p3d[25], p3d[27])
    elif mode == 'left_foot_angle':
        angle = angle_computation(p3d[26], p3d[28], p3d[32])
    elif mode == 'right_foot_angle':
        angle = angle_computation(p3d[25], p3d[27], p3d[31])
    elif mode == 'hip_left':
        angle = angle_computation(p3d[12], p3d[24], p3d[26])
    elif mode == 'hip_right':
        angle = angle_computation(p3d[11], p3d[23], p3d[25])
    elif mode == 'armpit_left':
        angle = angle_computation(p3d[14], p3d[12], p3d[24])
    elif mode == 'armpit_right':
        angle = angle_computation(p3d[13], p3d[11], p3d[23])
    return angle


def is_angle_wrong(p3d_r, p3d_q, mode='elbow_left', threshold=30):
    angle_r = show_predefined_angles(p3d_r, mode)
    angle_q = show_predefined_angles(p3d_q, mode)
    diff_angle = angle_q - angle_r
    if diff_angle > threshold:
        return 1
    elif diff_angle < -threshold:
        return -1
    else:
        return 0


def draw_wrong_angle(image_q, p3d_r, p3d_q, p2d_q, mode='elbow_left', threshold=30):
    j1, j2, j3 = joint_point[mode][1], joint_point[mode][0], joint_point[mode][2]
    is_wrong = is_angle_wrong(p3d_r, p3d_q, mode, threshold)
    if is_wrong == 0:
        return image_q
    elif is_wrong == 1:
        u, v = get_2d(p2d_q[j1])
        vis = p2d_q[j1][-1]
        if vis > 0.5:
            cv2.circle(image_q, (u, v), 5, (0, 0, 255), 2)
            draw_line_2d(image_q, p2d_q, j1, j2, (0, 0, 255))
            draw_line_2d(image_q, p2d_q, j1, j3, (0, 0, 255))
            cv2.putText(image_q, 'big', (u+5, v), cv2.FONT_HERSHEY_PLAIN, 1.5, (0, 127, 255), 2)
    elif is_wrong == -1:
        u, v = get_2d(p2d_q[j1])
        vis = p2d_q[j1][-1]
        if vis > 0.5:
            cv2.circle(image_q, (u, v), 5, (0, 0, 255), 2)
            draw_line_2d(image_q, p2d_q, j1, j2, (0, 0, 255))
            draw_line_2d(image_q, p2d_q, j1, j3, (0, 0, 255))
            cv2.putText(image_q, 'small', (u+5, v), cv2.FONT_HERSHEY_PLAIN, 1.5, (0, 127, 255), 2)

    return image_q


def draw_angles(image, p3d, p2d, h, w, mode='elbow_left', draw_mode=0):
    if len(p2d) == 0 or len(p3d) == 0:
        return image
    angle = show_predefined_angles(p3d, mode)

    if angle is None:
        return image

    if mode == 'elbow_left':
        u, v = get_2d(p2d[14])
        vis = p2d[14][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u + 5, v), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.2)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'elbow_right':
        u, v = get_2d(p2d[13])
        vis = p2d[13][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u + 5, v), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.25)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'chest_upper_left':
        u, v = get_2d(p2d[12])
        vis = p2d[12][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u - 5, v + 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.30)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'chest_upper_right':
        u, v = get_2d(p2d[11])
        vis = p2d[11][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u - 5, v - 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.35)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'chest_bottom_left':
        u, v = get_2d(p2d[24])
        vis = p2d[24][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u + 5, v + 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.40)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'chest_bottom_right':
        u, v = get_2d(p2d[23])
        vis = p2d[23][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u + 5, v - 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.45)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'left_big_leg_angle':
        u, v = get_2d(p2d[24])
        vis = p2d[24][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u - 5, v + 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.50)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'right_big_leg_angle':
        u, v = get_2d(p2d[23])
        vis = p2d[23][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u - 5, v - 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.55)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'left_knee_angle':
        u, v = get_2d(p2d[26])
        vis = p2d[26][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u, v + 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.60)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'right_knee_angle':
        u, v = get_2d(p2d[25])
        vis = p2d[25][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u, v - 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.65)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'left_foot_angle':
        u, v = get_2d(p2d[28])
        vis = p2d[28][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u, v - 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.70)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'right_foot_angle':
        u, v = get_2d(p2d[27])
        vis = p2d[27][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u, v + 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.75)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'hip_left':
        u, v = get_2d(p2d[24])
        vis = p2d[24][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u + 5, v + 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.80)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'hip_right':
        u, v = get_2d(p2d[23])
        vis = p2d[23][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u + 5, v + 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.85)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'armpit_left':
        u, v = get_2d(p2d[12])
        vis = p2d[12][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u - 5, v + 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.40)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    elif mode == 'armpit_right':
        u, v = get_2d(p2d[11])
        vis = p2d[11][-1]
        if vis > 0.5:
            if draw_mode == 0:
                cv2.putText(image, str(int(angle)), (u - 5, v + 5), cv2.FONT_HERSHEY_PLAIN, 1.5,
                            (0, 0, 255), 2)
                cv2.putText(image, mode + ": " + str(int(angle)), (int(w * 0.01), int(h * 0.45)),
                            cv2.FONT_HERSHEY_PLAIN, 1,
                            (0, 255, 0), 1)

    return image


def motion_similarity(coord_ref, coord_query, neighbour_dir, idx_list,predefine_angle_list,threshold):
    # differ
    total_diff = MotionInitialCheck(coord_ref=coord_ref,coord_query=coord_query,neighbour_dir=neighbour_dir,
                                    idx_list=idx_list)
    # mode
    mode = Status(total_diff)

    angle_mode_dict = dict()
    is_correct = True
    if mode==0:
        for joint_name in predefine_angle_list:
            angle_mode = is_angle_wrong(coord_ref,coord_query,mode=joint_name,threshold=threshold)
            if angle_mode !=0:
                is_correct = False
            angle_mode_dict[joint_name] = angle_mode
    else:
        is_correct = False

    return  is_correct, angle_mode_dict

def PixelWiseEncoding(p3d1, p3d2):
    """
    p3d1 is the reference
    p3d2 is the target
    """
    encoded = np.array([0, 0, 0])
    scence_coordinate_1 = get_3d(p3d1)
    scence_coordinate_2 = get_3d(p3d2)
    if scence_coordinate_1[0] - scence_coordinate_2[0] > 0:
        encoded[0] = 1
    if scence_coordinate_1[1] - scence_coordinate_2[1] > 0:
        encoded[1] = 1
    if scence_coordinate_1[0] - scence_coordinate_2[0] > 0:
        encoded[1] = 1
    return encoded


# Marker Space Coding
def SpaceEncoding(marker_id, neighbour_dir, scene_coord=None):
    """
    marker id is the idx for skeleton
    """
    try:
        reference_coordinate = scene_coord[marker_id]
        neighbour_ids = neighbour_dir[str(marker_id)]
        encodes = np.zeros([len(neighbour_ids), 3])
        for i, idx in enumerate(neighbour_ids):
            cur_neighbour_coord = scene_coord[idx]
            encode = PixelWiseEncoding(reference_coordinate, cur_neighbour_coord)
            encodes[i] = encode
    except:
        print("Make sure the marker id is among 11~32!")
        return

    return encodes


def CorrespondingPixelCost(coord_ref, coord_query, neighbour_dir, idx):
    encode_ref = SpaceEncoding(idx, neighbour_dir=neighbour_dir, scene_coord=coord_ref)
    encode_qurey = SpaceEncoding(idx, neighbour_dir=neighbour_dir, scene_coord=coord_query)
    diff = EncodingDiffer(encode_ref, encode_qurey)
    return diff


# simple cost computation
def EncodingDiffer(encode1, encode2):
    assert encode1.shape == encode2.shape
    h, w = encode1.shape
    total_error = np.sum(np.abs(encode1 - encode2))
    normalized_error = total_error * 1.0 / (h * w)
    return normalized_error


# Simple Motion Check
def MotionInitialCheck(coord_ref, coord_query, neighbour_dir, idx_list):
    if idx_list is None or len(idx_list) == 0:
        return 1000
    if len(coord_ref) == 0 or len(coord_query) == 0:
        return 1000

    total_differ = 0
    N = len(idx_list)

    adaptive_weight = [1.0 / N for _ in range(N)]

    for i, idx in enumerate(idx_list):
        differ = CorrespondingPixelCost(coord_ref=coord_ref, coord_query=coord_query, neighbour_dir=neighbour_dir,
                                        idx=idx)
        total_differ += differ * adaptive_weight[i]

    return total_differ


def Status(diff):
    if 0.05 < diff < 0.2:
        mode = 0
    elif diff >= 0.2:
        mode = 1
    else:
        mode = 0

    return mode


# can pass
def ShowStatus(diff, image_data, h, w):
    status_dict = {'0': "Correct", '1': "Not accuracy", '2': "Wrong Motion"}
    status = Status(diff)
    test = status_dict[str(status)]
    color = (0, 0, 0)
    if status == 0:
        color = (0, 255, 0)
    elif status == 1:
        color = (255, 0, 0)
    elif status == 2:
        color = (0, 0, 255)
    cv2.putText(image_data, test + str(round(diff, 5)), (int(w * 0.1), int(h * 0.1)), cv2.FONT_HERSHEY_PLAIN, 1,
                color, 1)

    return image_data


def StaticsJudge(pixel_coordinate: List):
    frame_nums = len(pixel_coordinate)
    reference_frame = pixel_coordinate[-1]

    # if there is no in last frame
    if len(reference_frame) == 0:
        if len(pixel_coordinate[-2]) == 0:
            return 0.0
        else:
            return 10000

    # Skip Faces
    reference_frame = reference_frame[11:]

    # Get Valid frames
    valid_frame_nums = 0
    displacement_total = 0

    for idx, previous in enumerate(pixel_coordinate):
        # Skip Last frames
        if idx == frame_nums - 1:
            continue
        # Make sure there is something at the frame
        if len(previous) == 0:
            continue
        # Skip the faces
        previous = previous[11:]
        total_displacement_x = 0
        total_displacement_y = 0
        for sub_id, pixels in enumerate(previous):
            pre_u, pred_v = get_2d(pixels)
            ref_u, ref_v = get_2d(reference_frame[sub_id])
            delta_u, delta_v = abs(pre_u - ref_u), abs(pred_v - ref_v)
            total_displacement_x += delta_u
            total_displacement_y += delta_v

        total_displacement_y = total_displacement_y * 1.0 / 22
        total_displacement_x = total_displacement_x * 1.0 / 22
        total_displacement = math.sqrt(total_displacement_x ** 2 + total_displacement_y ** 2)
        valid_frame_nums = valid_frame_nums + 1
        displacement_total += total_displacement

    if valid_frame_nums == 0:
        return 1000

    displacement_total = displacement_total * 1.0 / valid_frame_nums

    if displacement_total > 0.9:
        return False
    else:
        return True


# neighbour_dir = {'12': [14, 11, 24], '11': [12, 23, 13],
#                  '14': [12, 16], '16': [22, 18, 14],
#                  '18': [16, 20], '20': [18, 16], '22': [16],
#                  '13': [11, 15], '15': [13, 21, 17], '21': [15],
#                  '17': [15, 19], '19': [15, 17],
#                  '24': [12, 23], '23': [24, 11],
#                  '26': [24, 28], '25': [23, 27],
#                  '28': [26, 32, 30], '27': [25, 29, 31],
#                  '32': [28, 30], '31': [27, 29],
#                  '30': [28, 32],
#                  '29': [27, 31]
#                  }

neighbour_dir = {'11': [12, 23, 13], '12': [14, 11, 24],
                 '13': [11, 15], '14': [12, 16],
                 '15': [13], '16': [14],
                 '23': [11, 24, 25], '24': [12, 23, 26],
                 '25': [23, 27], '26': [24, 28],
                 '27': [25], '28': [26]
                 }

joint_point = {
    'elbow_left': [12, 14, 16],
    'elbow_right': [11, 13, 15],
    'armpit_left': [14, 12, 24],
    'armpit_right': [13, 11, 23],
    'hip_left': [12, 24, 26],
    'hip_right': [11, 23, 25],
    'left_knee_angle': [24, 26, 28],
    'right_knee_angle': [23, 25, 27]
}

if __name__ == "__main__":
    # pass

    # # p1 =[20,0.3,0.2,-0.3,1]
    # # p2 =[20,0.2,0.8,-0.9,1]
    # # p3 =[20,0.9,0.8,-0.0,1]
    p1 = [20, 0.3, 0.2, -0.3, 1]
    p2 = [20, 0.6, 0.4, -0.6, 1]
    p3 = [20, 0.9, 0.6, -0.9, 1]
    angle = angle_computation(p1, p2, p3)
    print(angle)
