var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":203,"id":1999,"methods":[{"el":59,"sc":2,"sl":56},{"el":67,"sc":2,"sl":64},{"el":89,"sc":4,"sl":87},{"el":105,"sc":4,"sl":102},{"el":112,"sc":4,"sl":108},{"el":118,"sc":4,"sl":115},{"el":125,"sc":2,"sl":75},{"el":161,"sc":2,"sl":133},{"el":201,"sc":2,"sl":169}],"name":"TestUserSessionServlet","sl":39}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_2":{"methods":[{"sl":108},{"sl":169}],"name":"TestLogout","pass":true,"statements":[{"sl":109},{"sl":110},{"sl":111},{"sl":171},{"sl":174},{"sl":175},{"sl":176},{"sl":179},{"sl":180},{"sl":181},{"sl":182},{"sl":183},{"sl":186},{"sl":187},{"sl":188},{"sl":189},{"sl":190},{"sl":191},{"sl":194},{"sl":195},{"sl":196},{"sl":197},{"sl":198},{"sl":199}]},"test_8":{"methods":[{"sl":87},{"sl":102},{"sl":108},{"sl":115},{"sl":133}],"name":"TestLogin","pass":true,"statements":[{"sl":88},{"sl":103},{"sl":104},{"sl":109},{"sl":110},{"sl":111},{"sl":116},{"sl":117},{"sl":135},{"sl":138},{"sl":139},{"sl":140},{"sl":143},{"sl":144},{"sl":147},{"sl":148},{"sl":149},{"sl":150},{"sl":153},{"sl":154},{"sl":155},{"sl":158},{"sl":159},{"sl":160}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [8], [8], [], [], [], [], [], [], [], [], [], [], [], [], [], [8], [8], [8], [], [], [], [2, 8], [2, 8], [2, 8], [2, 8], [], [], [], [8], [8], [8], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [8], [], [8], [], [], [8], [8], [8], [], [], [8], [8], [], [], [8], [8], [8], [8], [], [], [8], [8], [8], [], [], [8], [8], [8], [], [], [], [], [], [], [], [], [2], [], [2], [], [], [2], [2], [2], [], [], [2], [2], [2], [2], [2], [], [], [2], [2], [2], [2], [2], [2], [], [], [2], [2], [2], [2], [2], [2], [], [], [], []]
