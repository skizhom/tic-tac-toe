(ns tic-tac-toe.result)
;;  too lazy to find some library with this stuff

(defn success [x] {::state :success ::value x})
(defn faliure [x] {::state :error ::value x})

(defn success? [x] (= :success (::state x)))
(defn faliure? [x] (= :error (::state x)))

(defn value [x] (when (success? x) (::value x)))
(defn error [x] (when (faliure? x) (::value x)))

(defn bind [f]
  (fn [x & args]
    (if (success? x)
      (apply f (value x) args)
      x)))

(defn lift [f]
  (fn [x & args]
    (if (success? x)
      (success (apply f (value x) args))
      x)))
