predefined_angles = ['elbow_left', 'elbow_right',
                     'armpit_left', 'armpit_right',
                     'hip_left', 'hip_right',
                     'left_knee_angle', 'right_knee_angle']

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
    for joint in predefined_angles:
        print(joint_point[joint][1])
        joint1, joint2 = joint_point[joint][0], joint_point[joint][2]
        print(joint1, joint2)
